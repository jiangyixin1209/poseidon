package top.jiangyixin.poseidon.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Properties工具类
 * @author jiangyixin
 */
public class PropertyUtils {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
	public static final String FILE_PROTOCOL = "file:";

	/**
	 * 读取Properties
	 *
	 * 如果是磁盘上的文件则以file:开头，否则从classpath加载
	 * @param propertyFilename      Properties文件路径。
	 * @return                      Properties
	 */
	public static Properties readProperty(String propertyFilename) {
		if (StringUtils.isEmpty(propertyFilename)) {
			throw new PoseidonException("propertyFilename can not be empty");
		}
		if (propertyFilename.startsWith(FILE_PROTOCOL)) {
			propertyFilename = propertyFilename.substring(FILE_PROTOCOL.length());
			return readPropertyFile(propertyFilename);
		} else {
			return readPropertyByClasspath(propertyFilename);
		}

	}

	/**
	 * 通过Classpath加载Properties文件
	 * @param propertyFilename          Properties文件路径
	 * @return                          Properties
	 */
	public static Properties readPropertyByClasspath(String propertyFilename) {
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFilename);
			if (inputStream == null) {
				return null;
			}
			Properties properties = new Properties();
			properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			return properties;
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * 加载Properties文件
	 * @param propertyFilename      Properties文件路径
	 * @return                      Properties
	 */
	public static Properties readPropertyFile(String propertyFilename) {
		InputStream inputStream = null;
		try {
			File file = new File(propertyFilename);
			if (!file.exists()) {
				return null;
			}
			URL url = file.toURI().toURL();
			inputStream = new FileInputStream(url.getPath());
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * 将properties写入文件
	 * @param properties        Properties
	 * @param propertyFilename  Properties文件路径
	 * @return                  是否写入成功
	 */
	public static boolean writePropertyFile(Properties properties, String propertyFilename) {
		FileOutputStream fileOutputStream = null;
		try {
			File file = new File(propertyFilename);
			if (!file.exists()) {
				boolean mkdirs = file.getParentFile().mkdirs();
				if (!mkdirs) {
					throw new PoseidonException("mkdir dir error");
				}
			}
			fileOutputStream = new FileOutputStream(file, false);
			properties.store(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8), null);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return false;
	}

}
