package top.jiangyixin.poseidon.core.util;

/**
 * @author jiangyixin
 */
public class StringUtils {

	/**
	 * 判断字符串是否为空
	 * @param value     字符串
	 * @return          true/false
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
}
