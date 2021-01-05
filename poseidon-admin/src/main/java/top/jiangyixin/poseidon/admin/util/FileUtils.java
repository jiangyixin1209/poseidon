package top.jiangyixin.poseidon.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.admin.service.impl.ConfigServiceImpl;
import top.jiangyixin.poseidon.core.util.PropertyUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 配置文件 工具类
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/5 下午3:32
 */
public class FileUtils {
	private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	private static final String DEFAULT_KEY_NAME = "value";
	
	/**
	 * 获取配置文件完整本地路径地址
	 *
	 * 如果 env = test, project = lock, key = user
	 * 则本地文件为 {configFilePath}/test/lock_user.properties
	 *
	 * @param baseDir       根目录
	 * @param env           env
	 * @param project		project
	 * @param key           key
	 * @return              本地路径地址
	 */
	public static String getConfigFilepath(String baseDir, String env, String project, String key) {
		return baseDir.concat(File.separator).concat(env)
				.concat(File.separator).concat(project).concat("_").concat(key).concat(".properties");
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
	 * @param baseDir       根目录
	 * @param env           env
	 * @param project		project
	 * @param key           key
	 * @return              配置内容
	 */
	public static String getConfigFromFile(String baseDir, String env, String project, String key) {
		String configFilePath = getConfigFilepath(baseDir, env, project, key);
		Properties properties = PropertyUtils.readProperty(configFilePath);
		if (properties != null && properties.containsKey(DEFAULT_KEY_NAME)) {
			return properties.getProperty("value");
		}
		return null;
	}
	
	/**
	 * 清理被删除config的本地mirror文件
	 * @param configFileDir             根目录
	 * @param configDataFileList        现存config所有config file列表
	 */
	public static void cleanConfigFile(File configFileDir, final List<String> configDataFileList) {
		if (!configFileDir.exists() || configFileDir.list() == null
				|| Objects.requireNonNull(configFileDir.list()).length == 0) {
			return;
		}
		File[] configFileList = configFileDir.listFiles();
		for (File configFile: Objects.requireNonNull(configFileList)) {
			if (configFile.isFile() && !configDataFileList.contains(configFile.getPath())) {
				configFile.delete();
				logger.info("Poseidon cleanConfigFile delete configFile=[{}]", configFile.getPath());
			}
			if (configFile.isDirectory()) {
				if (configFile.listFiles() != null
						&& Objects.requireNonNull(configFile.listFiles()).length > 0) {
					cleanConfigFile(configFile, configDataFileList);
				} else {
					configFile.delete();
				}
			}
		}
	}
}
