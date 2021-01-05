package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import top.jiangyixin.poseidon.admin.constant.Constant;
import top.jiangyixin.poseidon.admin.mapper.*;
import top.jiangyixin.poseidon.admin.pojo.entity.*;
import top.jiangyixin.poseidon.admin.pojo.param.ConfigParam;
import top.jiangyixin.poseidon.admin.pojo.query.ConfigQuery;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.ConfigNotifyService;
import top.jiangyixin.poseidon.admin.service.ConfigService;
import top.jiangyixin.poseidon.admin.service.UserService;
import top.jiangyixin.poseidon.admin.util.FileUtils;
import top.jiangyixin.poseidon.admin.util.PojoUtils;
import top.jiangyixin.poseidon.core.util.PropertyUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * 配置 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
	private final static Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
	
	@Resource
	private UserService userService;
	@Resource
	private ProjectMapper projectMapper;
	@Resource
	private EnvMapper envMapper;
	@Resource
	private ConfigLogMapper configLogMapper;
	@Resource
	private ConfigNotifyService configNotifyService;
	/**
	 * 访问access token
	 */
	@Value("${poseidon.access.token}")
	private String accessToken;
	
	/**
	 * 本地配置文件存放根目录
	 */
	@Value("${poseidon.conf.dir}")
	private String configDir;
	/**
	 * DeferredResult，存放各个客户端对某个key的监控
	 */
	private final Map<String, List<DeferredResult<R<String>>>> configDeferredResult = new ConcurrentHashMap<>();
	/**
	 * 已读 configNotify id列表
	 */
	private final List<Long> readNotifyIdList = Collections.synchronizedList(new ArrayList<>());
	
	@Override
	public R<String> add(ConfigParam configParam, User user) {
		Config config = PojoUtils.copy(configParam, Config.class);
		if (config == null) {
			return R.fail("Error");
		}
		if (userService.hasProjectPermission(user, config)) {
			return R.fail("您没有该项目的配置权限,请联系管理员开通");
		}
		Env env = envMapper.selectOne(new QueryWrapper<Env>()
				.eq("code", config.getEnv()));
		if (env == null) {
			return R.fail("Env [" + config.getEnv() + "]不存在");
		}
		Project project = projectMapper.selectOne(new QueryWrapper<Project>()
				.eq("code", config.getProject()));
		if (project == null) {
			return R.fail("Project [" + config.getProject() + "]不存在");
		}
		// 去除key中的空格
		config.setKey(config.getKey().trim());
		Config cfg = getOne(new QueryWrapper<Config>()
				.eq("env", config.getEnv())
				.eq("project", config.getProject())
				.eq("`key`", config.getKey()));
		if (cfg != null) {
			return R.fail("配置Key已经存在，不可重复添加");
		}
		if (config.getValue() == null) {
			config.setValue("");
		}
		save(config);

		// 创建config change log
		ConfigLog configLog = new ConfigLog();
		configLog.setEnv(config.getEnv());
		configLog.setProject(config.getProject());
		configLog.setKey(config.getKey());
		configLog.setDescription(config.getDescription().concat("[新增配置]"));
		configLog.setNewValue(config.getValue());
		configLog.setOptUser(user.getUsername());
		configLogMapper.insert(configLog);

		// config notify
		configNotifyService.add(config.getEnv(), config.getProject(), config.getKey(), config.getValue());

		return R.success();
	}

	@Override
	public R<String> update(Config config, User user) {
		if (userService.hasProjectPermission(user, config)) {
			return R.fail("您没有该项目的配置权限,请联系管理员开通");
		}
		Config existConfig = getOne(new QueryWrapper<Config>()
				.eq("env", config.getEnv())
				.eq("project", config.getProject())
				.eq("key", config.getKey())
		);
		if (existConfig == null) {
			return R.fail("Key不存在，非法Key");
		}

		if (config.getValue() == null) {
			config.setValue("");
		}
		String oldValue = existConfig.getValue();
		existConfig.setDescription(config.getDescription());
		existConfig.setValue(config.getValue());
		boolean res = updateById(existConfig);
		if (!res) {
			return R.fail();
		}
		// 创建config change log
		ConfigLog configLog = new ConfigLog();
		configLog.setEnv(config.getEnv());
		configLog.setProject(config.getProject());
		configLog.setKey(config.getKey());
		configLog.setDescription(config.getDescription().concat("[更新配置]"));
		configLog.setOldValue(oldValue);
		configLog.setNewValue(config.getValue());
		configLogMapper.insert(configLog);

		// config notify
		configNotifyService.add(existConfig.getEnv(), existConfig.getProject(), existConfig.getKey(), existConfig.getValue());
		return R.success();
	}

	@Override
	public R<String> delete(Config config, User user) {
		if (userService.hasProjectPermission(user, config)) {
			return R.fail("您没有该项目的配置权限,请联系管理员开通");
		}
		remove(new QueryWrapper<Config>()
				.eq("env", config.getEnv())
				.eq("project", config.getProject())
				.eq("key", config.getKey())
		);
		config.setValue(null);
		configNotifyService.add(config.getEnv(), config.getProject(), config.getKey(), null);
		return R.success();
	}

	@Override
	public R<?> get(ConfigQuery configQuery) {

		// 校验参数
		if (!StringUtils.isEmpty(this.accessToken) && !this.accessToken.equals(configQuery.getAccessToken())) {
			return R.fail("AccessToken invalid!");
		}
		if (StringUtils.isEmpty(configQuery.getEnv())) {
			return R.fail("Env invalid!");
		}
		if (configQuery.getKeys() == null || configQuery.getKeys().size() == 0) {
			return R.fail("Keys invalid!");
		}
		
		// 从本地properties文件中读取配置
		Map<String, String> result = new HashMap<>(configQuery.getKeys().size());
		for (String key : configQuery.getKeys()) {
			// user_lock =>  project=user, key=lock
			String[] s = key.split("_");
			if (s.length != 2) {
				logger.error("illegal key : {}", key);
				continue;
			}
			String value = FileUtils.getConfigFromFile(this.configDir, configQuery.getEnv(), s[0], s[1]);
			if (value == null) {
				value = "";
			}
			result.put(key, value);
		}
		return new R<>(R.SUCCESS_CODE, result);
	}
	
	@Override
	public DeferredResult<R<String>> monitor(ConfigQuery configQuery) {
		DeferredResult<R<String>> deferredResult =
				new DeferredResult<>(Constant.DEFAULT_CLEAN_TIME * 1000L, R.success());
		if (accessToken != null && accessToken.trim().length() > 0 && !accessToken.equals(configQuery.getAccessToken())) {
			deferredResult.setResult(R.fail("AccessToken Invalid!"));
			return deferredResult;
		}
		if (configQuery.getEnv() == null || configQuery.getEnv().trim().length() == 0) {
			deferredResult.setResult(R.fail("Env Invalid!"));
			return deferredResult;
		}
		if (configQuery.getKeys() == null || configQuery.getKeys().size() == 0) {
			deferredResult.setResult(R.fail("Keys Invalid!"));
			return deferredResult;
		}
		for (String key : configQuery.getKeys()) {
			String[] result = key.split("_");
			String configFilepath = FileUtils.getConfigFilepath(this.configDir, configQuery.getEnv(), result[0], result[1]);
			List<DeferredResult<R<String>>> deferredResultList = configDeferredResult
					.computeIfAbsent(configFilepath, k -> new ArrayList<>());
			configDeferredResult.put(configFilepath, deferredResultList);
		}
		return deferredResult;
	}
	
	@Override
	public List<Config> listByPage(int page, int pageSize, QueryWrapper<Config> configQueryWrapper) {
		Page<Config> configPage = new Page<>(page, pageSize);
		this.baseMapper.selectPage(configPage, configQueryWrapper);
		return configPage.getRecords();
	}
	
	/**
	 * 将配置写入本地文件
	 * @param env               env
	 * @param project			project
	 * @param key               key
	 * @param value             value
	 * @return                  本地文件路径
	 */
	public String writeConfigToFile(String env,  String project, String key, String value) {
		String configFilepath = FileUtils.getConfigFilepath(this.configDir, env, project, key);
		Properties properties = PropertyUtils.readProperty(configFilepath);
		
		//重复更新
		if (properties != null && value != null && value.equals(properties.getProperty(Constant.DEFAULT_KEY_NAME))) {
			return new File(configFilepath).getPath();
		}
		
		// 写入本地 Properties 文件
		Properties newProperties = new Properties();
		if (value == null) {
			newProperties.setProperty("value-deleted", "true");
		} else {
			newProperties.setProperty("value", value);
		}
		PropertyUtils.writePropertyFile(newProperties, configFilepath);
		logger.info("Poseidon setConfigToFile: configFilepath=[{}], value=[{}]", configFilepath, value);
		
		// 广播通知client客户端
		List<DeferredResult<R<String>>> deferredResults = configDeferredResult.get(configFilepath);
		if (deferredResults != null) {
			configDeferredResult.remove(configFilepath);
			for (DeferredResult<R<String>> result : deferredResults) {
				result.setResult(R.success(key));
			}
		}
		return new File(configFilepath).getPath();
	}
	
	public List<Long> getReadNotifyIdList() {
		return readNotifyIdList;
	}
	
	public String getConfigDir() {
		return configDir;
	}
}
