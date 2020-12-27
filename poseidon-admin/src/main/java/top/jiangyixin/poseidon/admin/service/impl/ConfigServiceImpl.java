package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import top.jiangyixin.poseidon.admin.mapper.*;
import top.jiangyixin.poseidon.admin.pojo.entity.*;
import top.jiangyixin.poseidon.admin.pojo.query.ConfigQuery;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.ConfigService;
import top.jiangyixin.poseidon.admin.service.UserService;
import top.jiangyixin.poseidon.core.util.PropertyUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * 配置 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService, InitializingBean, DisposableBean {
	
	private final UserService userService;
	private final ConfigMapper configMapper;
	private final ProjectMapper projectMapper;
	private final EnvMapper envMapper;
	private final ConfigNotifyMapper configNotifyMapper;
	private final ConfigLogMapper configLogMapper;
	private final static Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
	public final static String DEFAULT_KEY_NAME = "value";
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
	private final ExecutorService executorService = Executors.newScheduledThreadPool(5);
	/**
	 * 是否轮询监控配置有更新
	 */
	private volatile boolean isLoopMonitor = true;
	/**
	 * 已读 configNotify id列表
	 */
	private final List<Long> readNotifyIdList = Collections.synchronizedList(new ArrayList<>());
	/**
	 * 每30秒清理一次 readNotifyIdList
	 */
	private final static int DEFAULT_CLEAN_TIME = 30;
	
	@Autowired
	public ConfigServiceImpl(UserService userService, ConfigMapper configMapper,
	                         ProjectMapper projectMapper, EnvMapper envMapper,
							 ConfigNotifyMapper configNotifyMapper, ConfigLogMapper configLogMapper) {
		this.userService = userService;
		this.configMapper = configMapper;
		this.projectMapper = projectMapper;
		this.envMapper = envMapper;
		this.configNotifyMapper = configNotifyMapper;
		this.configLogMapper = configLogMapper;
	}
	
	@Override
	public R<String> add(Config config, User user) {
		if (StringUtils.isEmpty(config.getEnv())) {
			return R.fail("ENV不能为空");
		}
		if (StringUtils.isEmpty(config.getProject())) {
			return R.fail("Project不能为空");
		}
		if (StringUtils.isEmpty(config.getKey())) {
			return R.fail("Key不能为空");
		}
		if (StringUtils.isEmpty(config.getDesc())) {
			return R.fail("配置描述不能为空");
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
		Config cfg = configMapper.selectOne(new QueryWrapper<Config>()
				.eq("env", config.getEnv())
				.eq("project", config.getProject())
				.eq("key", config.getKey()));
		if (cfg != null) {
			return R.fail("配置Key已经存在，不可重复添加");
		}
		if (config.getValue() == null) {
			config.setValue("");
		}

		configMapper.insert(config);

		// 创建config change log
		ConfigLog configLog = new ConfigLog();
		configLog.setEnv(config.getEnv());
		configLog.setProject(config.getProject());
		configLog.setKey(config.getKey());
		configLog.setDesc(config.getDesc().concat("[新增配置]"));
		configLog.setNewValue(config.getValue());
		configLogMapper.insert(configLog);

		// config notify
		addConfigNotify(config.getEnv(), config.getProject(), config.getKey(), config.getValue());

		return R.success();
	}

	@Override
	public R<String> update(Config config, User user) {
		if (StringUtils.isEmpty(config.getProject())) {
			return R.fail("Project不能为空");
		}
		if (StringUtils.isEmpty(config.getEnv())) {
			return R.fail("Env不能为空");
		}
		if (StringUtils.isEmpty(config.getDesc())) {
			return R.fail("配置描述不能为空");
		}
		if (userService.hasProjectPermission(user, config)) {
			return R.fail("您没有该项目的配置权限,请联系管理员开通");
		}
		Config existConfig = configMapper.selectOne(new QueryWrapper<Config>()
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
		existConfig.setDesc(config.getDesc());
		existConfig.setValue(config.getValue());
		int res = configMapper.updateById(existConfig);
		if (res < 1) {
			return R.fail();
		}
		// 创建config change log
		ConfigLog configLog = new ConfigLog();
		configLog.setEnv(config.getEnv());
		configLog.setProject(config.getProject());
		configLog.setKey(config.getKey());
		configLog.setDesc(config.getDesc().concat("[更新配置]"));
		configLog.setOldValue(oldValue);
		configLog.setNewValue(config.getValue());
		configLogMapper.insert(configLog);

		// config notify
		addConfigNotify(existConfig.getEnv(), existConfig.getProject(), existConfig.getKey(), existConfig.getValue());
		return R.success();
	}

	@Override
	public R<String> delete(Config config, User user) {
		if (StringUtils.isEmpty(config.getEnv())) {
			return R.fail("Env不能为空");
		}
		if (StringUtils.isEmpty(config.getProject())) {
			return R.fail("Project不能为空");
		}
		if (StringUtils.isEmpty(config.getKey())) {
			return R.fail("Key不能为空");
		}
		if (userService.hasProjectPermission(user, config)) {
			return R.fail("您没有该项目的配置权限,请联系管理员开通");
		}
		configMapper.delete(new QueryWrapper<Config>()
				.eq("env", config.getEnv())
				.eq("project", config.getProject())
				.eq("key", config.getKey())
		);
		config.setValue(null);
		addConfigNotify(config.getEnv(), config.getProject(), config.getKey(), null);
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
			String value = getConfigFromFile(configQuery.getEnv(), s[0], s[1]);
			if (value == null) {
				value = "";
			}
			result.put(key, value);
		}
		return new R<>(R.SUCCESS_CODE, result);
	}
	
	@Override
	public DeferredResult<R<String>> monitor(ConfigQuery configQuery) {
		return null;
	}

	@Override
	public void destroy() throws Exception {
		stopLoopMonitor();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		startLoopMonitor();
	}
	
	/**
	 * 从本地文件读取配置文件
	 *
	 * 如果 env = test, project = lock, key = user
	 * 则本地文件为 {configFilePath}/test/lock_user.properties
	 *
	 * lock_user.properties 文件内容为
	 * value={value}
	 *
	 * @param env           env
	 * @param project		project
	 * @param key           key
	 * @return              配置内容
	 */
	public String getConfigFromFile(String env, String project, String key) {
		String configFilePath = getConfigFilepath(env, project, key);
		Properties properties = PropertyUtils.readProperty(configFilePath);
		if (properties != null && properties.containsKey(DEFAULT_KEY_NAME)) {
			return properties.getProperty("value");
		}
		return null;
	}
	
	/**
	 * 获取配置文件完整本地路径地址
	 *
	 * 如果 env = test, project = lock, key = user
	 * 则本地文件为 {configFilePath}/test/lock_user.properties
	 *
	 * @param env           env
	 * @param project		project
	 * @param key           key
	 * @return              本地路径地址
	 */
	public String getConfigFilepath(String env, String project, String key) {
		return this.configDir.concat(File.separator).concat(env)
				.concat(File.separator).concat(project).concat("_").concat(key).concat(".properties");
	}
	
	/**
	 * 将配置写入本地文件
	 * @param env               env
	 * @param project			project
	 * @param key               key
	 * @param value             value
	 * @return                  本地文件路径
	 */
	public String writeConfigToFile(String env, String key, String project, String value) {
		String configFilepath = getConfigFilepath(env, project, key);
		Properties properties = PropertyUtils.readProperty(configFilepath);
		
		//重复更新
		if (properties != null && value != null && value.equals(properties.getProperty("value"))) {
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

	/**
	 * 新增ConfigNotify
	 * @param env			env
	 * @param project		project
	 * @param key			key
	 * @param value			value
	 */
	public void addConfigNotify(String env, String project, String key, String value) {
		ConfigNotify configNotify = new ConfigNotify();
		configNotify.setEnv(env);
		configNotify.setProject(project);
		configNotify.setKey(key);
		configNotify.setValue(value);
		configNotifyMapper.insert(configNotify);
	}

	public void startLoopMonitor() {
		executorService.execute(() -> {
			while (isLoopMonitor) {
				try {
					List<ConfigNotify> notifyList = configNotifyMapper.selectBatchIds(readNotifyIdList);
					if (notifyList != null && notifyList.size() > 0) {
						for (ConfigNotify notify : notifyList) {
							readNotifyIdList.add(notify.getId());
							// sync local file
							writeConfigToFile(notify.getEnv(), notify.getProject(), notify.getKey(),notify.getValue());
						}
					}
					// 清除旧的configNotify
					if ((System.currentTimeMillis() / 1000) % DEFAULT_CLEAN_TIME == 0) {
						configNotifyMapper.cleanExpireNotify(DEFAULT_CLEAN_TIME);
						readNotifyIdList.clear();
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				// 一秒执行一次
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					if (isLoopMonitor) {
						logger.error(e.getMessage());
					}
				}
			}
		});
	}

	public void stopLoopMonitor() {
		isLoopMonitor = false;
		executorService.shutdown();
	}
}
