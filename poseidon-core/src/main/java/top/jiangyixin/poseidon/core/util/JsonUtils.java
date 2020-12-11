package top.jiangyixin.poseidon.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json工具类
 * @author jiangyixin
 */
public class JsonUtils {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	static {
		// 对于空的对象转json的时候不抛出错误
		MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// 允许属性名称没有引号
		MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 允许单引号
		MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 设置输出时包含属性的风格
		MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	public static ObjectMapper getMapper() {
		return MAPPER;
	}

	/**
	 * 序列化，将对象转化为json字符串
	 * @param obj   对象
	 * @return      json字符串
	 */
	public static String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		String json = null;
		try {
			json = getMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("[{}] toJsonString error：{{}}", obj.getClass().getSimpleName(), e);
		}
		return json;
	}

	/**
	 * 反序列化，json字符串转为对象
	 * @param json      json字符串
	 * @param clazz     对象
	 * @param <T>       对象类型
	 * @return          反序列化对象
	 */
	public static <T> T parse(String json, Class<T> clazz) {
		T t = null;
		try {
			t = getMapper().readValue(json, clazz);
		} catch (Exception e) {
			logger.error(" parse json [{}] to class [{}] error：{{}}", json, clazz.getSimpleName(), e);
		}
		return t;
	}
}
