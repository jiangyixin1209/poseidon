package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
}
