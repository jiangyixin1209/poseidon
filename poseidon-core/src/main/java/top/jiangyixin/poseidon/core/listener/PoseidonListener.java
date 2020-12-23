package top.jiangyixin.poseidon.core.listener;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/22 下午4:38
 */
public interface PoseidonListener {
	
	/**
	 * 第一次使用或者发生变更时候回调
	 * @param key               key
	 * @param value             value
	 * @throws Exception        Exception
	 */
	void onChange(String key, String value) throws Exception;
}
