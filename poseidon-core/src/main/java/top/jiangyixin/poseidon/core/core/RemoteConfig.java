package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;
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

	public static Map remote(String url, String body) {
		try {
			String s = HttpUtils.doPost(url, body);
			if (StringUtils.isEmpty(s)) {
				return null;
			}
			Map<String, Object> response = (Map<String, Object>)JsonUtils.parse(s, Map.class);
			int code = Integer.parseInt(String.valueOf(response.get("code")));
			if (code != 200) {
				logger.error("request fail, msg={}", (response.getOrDefault("message", response)));
				return null;
			}
			return response;
		} catch (Exception e) {
			logger.error("remote get data error, url:{},body:{}", url, body);
			return null;
		}
	}

	public static String get(String key) {
		Map<String, String> result = RemoteConfig.get(new HashSet<String>(Collections.singletonList(key)));
		if (result != null) {
			return result.get(key);
		}
		return null;
	}

	public static Map<String, String> get(Set<String> keys) {
		for (String addressUrl : RemoteConfig.addressList) {
			String url = addressUrl + "/conf/get";
			ConfigParamDTO configParamDTO = new ConfigParamDTO();
			configParamDTO.setAccessToken(RemoteConfig.accessToken);
			configParamDTO.setEnv(RemoteConfig.env);
			configParamDTO.setKeys(new ArrayList<>(keys));
			String jsonBody = JsonUtils.toJsonString(configParamDTO);
			Map remote = RemoteConfig.remote(addressUrl, jsonBody);
			if (remote != null && remote.containsKey("data")) {
				Map<String, String> data = (Map<String, String>) remote.get("data");
				return data;
			}
		}
		return null;
	}

}
