package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;
import top.jiangyixin.poseidon.core.util.PropertyUtils;
import top.jiangyixin.poseidon.core.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author jiangyixin
 */
public class MirrorConfig {
	private static final Logger logger = LoggerFactory.getLogger(MirrorConfig.class);
	private static String mirrorFile;

	public static void init(String mirrorFile) {
		if (StringUtils.isEmpty(mirrorFile)) {
			throw new PoseidonException("MirrorFile can not be empty");
		}
		MirrorConfig.mirrorFile = mirrorFile;
	}

	/**
	 * 读取本地镜像缓存配置文件
	 * @return              Map<String, String>
	 */
	public static Map<String, String> readMirrorConfig() {
		Properties mirrorProperties = PropertyUtils.readProperty(mirrorFile);
		if (mirrorProperties == null) {
			return null;
		}
		Set<String> propertyKeys = mirrorProperties.stringPropertyNames();
		if (propertyKeys != null && propertyKeys.size() > 0) {
			Map<String, String> mirrorConfigData = new HashMap<>(propertyKeys.size());
			for (String key : propertyKeys) {
				mirrorConfigData.put(key, mirrorProperties.getProperty(key));
			}
			return mirrorConfigData;
		}
		return null;
	}

	/**
	 * 更新本地镜像缓存配置文件
	 * @param mirrorConfigData      镜像配置数据
	 */
	public static void writeMirrorConfig(Map<String, String> mirrorConfigData) {
		Properties properties = new Properties();
		for (Map.Entry<String, String> config : mirrorConfigData.entrySet()) {
			properties.setProperty(config.getKey(), config.getValue());
		}
		PropertyUtils.writePropertyFile(properties, mirrorFile);
	}
}
