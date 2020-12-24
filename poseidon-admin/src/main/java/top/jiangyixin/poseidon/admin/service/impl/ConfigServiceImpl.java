package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import top.jiangyixin.poseidon.admin.entity.Config;
import top.jiangyixin.poseidon.admin.mapper.ConfigMapper;
import top.jiangyixin.poseidon.admin.pojo.query.ConfigQuery;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.ConfigService;
import top.jiangyixin.poseidon.core.util.PropertyUtils;
import top.jiangyixin.poseidon.core.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
	
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
	 * 延迟Result，存放各个客户端对某个key的监控
	 */
	private final Map<String, List<DeferredResult<R<String>>>> configDeferredResult = new ConcurrentHashMap<>();
	
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
			String value = getConfigFromFile(configQuery.getEnv(), key);
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
	 * @param key           key
	 * @return              配置内容
	 */
	public String getConfigFromFile(String env, String key) {
		String configFilePath = getConfigFilepath(env, key);
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
	 * @param key           key
	 * @return              本地路径地址
	 */
	public String getConfigFilepath(String env, String key) {
		return this.configDir.concat(File.separator).concat(env)
				.concat(File.separator).concat(key).concat(".properties");
	}
	
	/**
	 * 将配置写入本地文件
	 * @param env               env
	 * @param key               key
	 * @param value             value
	 * @return                  本地文件路径
	 */
	public String writeConfigToFile(String env, String key, String value) {
		String configFilepath = getConfigFilepath(env, key);
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
	
}
