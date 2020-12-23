package top.jiangyixin.poseidon.core;

import top.jiangyixin.poseidon.core.core.LocalCacheConfig;
import top.jiangyixin.poseidon.core.exception.PoseidonException;
import top.jiangyixin.poseidon.core.listener.PoseidonListener;
import top.jiangyixin.poseidon.core.listener.PoseidonListenerManager;

/**
 * Poseidon Client
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/22 下午4:44
 */
public class PoseidonClient {
	
	public static String get(String key, String defaultValue) {
		return LocalCacheConfig.get(key, defaultValue);
	}
	
	public static String get(String key) {
		return LocalCacheConfig.get(key, null);
	}
	
	public static boolean getBoolean(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new PoseidonException("config key [" + key + "] does not exit");
		}
		return Boolean.parseBoolean(value);
	}
	
	public static short getShort(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new PoseidonException("config key [" + key + "] does not exit");
		}
		return Short.parseShort(value);
	}
	
	public static long getLong(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new PoseidonException("config key [" + key + "] does not exit");
		}
		return Long.parseLong(value);
	}
	
	public static float getFloat(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new PoseidonException("config key [" + key + "] does not exit");
		}
		return Float.parseFloat(value);
	}
	
	public static double getDouble(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new PoseidonException("config key [" + key + "] does not exit");
		}
		return Double.parseDouble(value);
	}
	
	public static boolean addListener(String key, PoseidonListener listener) {
		return PoseidonListenerManager.addListener(key, listener);
	}
}
