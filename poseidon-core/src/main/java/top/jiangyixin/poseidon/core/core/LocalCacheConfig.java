package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.common.SetType;
import top.jiangyixin.poseidon.core.exception.PoseidonException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangyixin
 */
public class LocalCacheConfig {
	private static final Logger logger = LoggerFactory.getLogger(LocalCacheConfig.class);

	private static ConcurrentHashMap<String, String> localCache = null;
	private static boolean refreshThreadStop = false;
	private static Thread refreshThread;
	private static boolean initOk = false;


	public static void init() {
		if (initOk) {
			throw new PoseidonException("RemoteConfig only can be init once !");
		}
		localCache = new ConcurrentHashMap<>();

		Map<String, String> configData = new HashMap<>();
		Map<String, String> mirrorConfigData = MirrorConfig.readMirrorConfig();

		if (mirrorConfigData != null && mirrorConfigData.size() > 0) {
			configData.putAll(mirrorConfigData);
		}

		for (String key : configData.keySet()) {

		}

		initOk = true;
	}

	public static void destroy() {
		if (refreshThread != null) {
			refreshThreadStop = true;
			refreshThread.interrupt();
		}
	}
	public static void startRefreshThread() {
		refreshThread = new Thread(() -> {
			while (!refreshThreadStop) {

			}
		});
		refreshThread.setDaemon(true);
		refreshThread.start();
		logger.info(">>>>>>>>>> RemoteConfig init success");

	}
	
	private static void refreshCacheAndMirror() throws InterruptedException {
		if (localCache.size() == 0) {
			TimeUnit.SECONDS.sleep(3);
			return;
		}
	}

	private static void set(String key, String value, SetType setType) {
		localCache.put(key, value);
		logger.info("poseidon: {} : [{}={}]", setType, key, value);

		if (setType == SetType.SET) {
		} else if (setType == SetType.RELOAD) {
			refreshThread.interrupt();
		}

	}

	/**
	 * 从 local cache 中获取配置
	 * @param key       配置key
	 * @return          配置value
	 */
	private static String get(String key) {
		if (localCache.containsKey(key)) {
			return localCache.get(key);
		}
		return null;
	}
	
	/**
	 * 获取缓存
	 * @param key               cache key
	 * @param defaultValue      default value
	 * @return                  cache value
	 */
	public static String get(String key, String defaultValue) {
		// 1、先从local cache 中获取缓存
		String value = get(key);
		if (value != null) {
			return value;
		}
		// 2、从远程获取
		String remoteValue = null;
		try {
			remoteValue = RemoteConfig.get(key);
		} catch (Exception e) {
			logger.error("Get {} from remote fail, {}", key, e.getMessage());
		}
		// 缓存一份到 local cache，支持缓存值为null
		set(key, remoteValue, SetType.SET);
		if (remoteValue != null) {
			return remoteValue;
		}
		return defaultValue;
	}

}
