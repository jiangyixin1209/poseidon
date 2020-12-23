package top.jiangyixin.poseidon.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.PoseidonClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Poseidon Listener Manage
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/22 下午4:37
 */
public class PoseidonListenerManager {
	private final static Logger logger = LoggerFactory.getLogger(PoseidonListenerManager.class);
	private final static ConcurrentHashMap<String, List<PoseidonListener>> KEY_LISTENER = new ConcurrentHashMap<>();
	private final static List<PoseidonListener> NO_KEY_LISTENER = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * 添加 key 监听器，执行回调并且加入监听列表
	 * @param key                   key
	 * @param listener              listener
	 * @return                      true/false
	 */
	public static boolean addListener(String key, PoseidonListener listener) {
		if (listener == null) {
			return false;
		}
		if (key == null || key.trim().length() == 0) {
			NO_KEY_LISTENER.add(listener);
			return true;
		}
		// 触发回调
		try {
			String value = PoseidonClient.get(key);
			listener.onChange(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		// 加入监听列表
		List<PoseidonListener> listeners = KEY_LISTENER.computeIfAbsent(key, k -> new ArrayList<>());
		listeners.add(listener);
		return true;
	}
	
	/**
	 * 触发config变更回调
	 * @param key                   key
	 * @param value                 value
	 */
	public static void onChange(String key, String value) {
		if (key == null || key.trim().length() == 0) {
			return;
		}
		List<PoseidonListener> listeners = KEY_LISTENER.get(key);
		for (PoseidonListener listener : listeners) {
			try {
				listener.onChange(key, value);
			} catch (Exception e) {
				logger.error("Execute key<{}> listener error {}", key, e.getMessage());
			}
		}
		
		for (PoseidonListener listener : NO_KEY_LISTENER) {
			try {
				listener.onChange(key, value);
			} catch (Exception e) {
				logger.error("Execute no key listener error {}", e.getMessage());
			}
		}
	}
	
	
}
