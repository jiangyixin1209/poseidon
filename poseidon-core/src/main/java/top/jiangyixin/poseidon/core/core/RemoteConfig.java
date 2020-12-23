package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;
import top.jiangyixin.poseidon.core.pojo.ConfigDTO;
import top.jiangyixin.poseidon.core.pojo.ConfigParamDTO;
import top.jiangyixin.poseidon.core.util.HttpUtils;
import top.jiangyixin.poseidon.core.util.JsonUtils;
import top.jiangyixin.poseidon.core.util.StringUtils;

import java.util.*;

/**
 * @author jiangyixin
 */
public class RemoteConfig {
	private static final Logger logger = LoggerFactory.getLogger(RemoteConfig.class);

	private static String address;
	private static String env;
	private static String accessToken;
	private static List<String> addressList = null;

	public static void init(String address, String env, String accessToken) {
		if (StringUtils.isEmpty(address)) {
			throw new PoseidonException("Poseidon: address can not be empty");
		}
		if (StringUtils.isEmpty(env)) {
			throw new PoseidonException("Poseidon: env can not be empty");
		}

		addressList = new ArrayList<>();
		RemoteConfig.address = address;
		RemoteConfig.env = env;
		RemoteConfig.accessToken = accessToken;
		RemoteConfig.addressList.addAll(Arrays.asList(address.split(",")));
	}
	
	/**
	 * 远程请求
	 * @param url               中心服务器地址
	 * @param body              请求参数
	 * @return                  List<ConfigDTO.ConfigVO>
	 */
	public static List<ConfigDTO.ConfigVO> remote(String url, String body) {
		try {
			String s = HttpUtils.doPost(url, body);
			if (StringUtils.isEmpty(s)) {
				return null;
			}
			ConfigDTO configDTO = JsonUtils.parse(s, ConfigDTO.class);
			if (configDTO.getCode() != 200) {
				logger.error("request fail, msg={}", configDTO.getMessage());
				return null;
			}
			return configDTO.getConfigList();
		} catch (Exception e) {
			logger.error("remote get data error, url:{},body:{}", url, body);
			return null;
		}
	}
	
	/**
	 * 获取单个key的值
	 * @param key               key
	 * @return                  value
	 */
	public static String get(String key) {
		List<ConfigDTO.ConfigVO> voList = RemoteConfig.get(new HashSet<String>(Collections.singletonList(key)));
		if (voList != null && !voList.isEmpty()) {
			return voList.get(0).getValue();
		}
		return null;
	}
	
	/**
	 * 远程获取 key 集合的值
	 * @param keys              key set
	 * @return                  value list
	 */
	public static List<ConfigDTO.ConfigVO> get(Set<String> keys) {
		for (String addressUrl : RemoteConfig.addressList) {
			String url = addressUrl + "/conf/get";
			ConfigParamDTO configParamDTO = new ConfigParamDTO();
			configParamDTO.setAccessToken(RemoteConfig.accessToken);
			configParamDTO.setEnv(RemoteConfig.env);
			configParamDTO.setKeys(new ArrayList<>(keys));
			String jsonBody = JsonUtils.toJsonString(configParamDTO);
			List<ConfigDTO.ConfigVO> configList = RemoteConfig.remote(url, jsonBody);
			if (configList != null && !configList.isEmpty()) {
				return configList;
			}
		}
		return null;
	}

}
