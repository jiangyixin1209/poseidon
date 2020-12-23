package top.jiangyixin.poseidon.admin.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/23 下午2:26
 */
public class PojoUtil {
	
	public static <T, F> List<F> copyList(List<T> origins, Class<F> clazz) {
		List<F> results = new ArrayList<>();
		for (T t : origins) {
			F f = copy(t, clazz);
			results.add(f);
		}
		return results;
	}
	
	public static <T, F> F copy(T origin, Class<F> clazz) {
		try {
			F f = clazz.newInstance();
			BeanUtils.copyProperties(origin, f);
			return f;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
