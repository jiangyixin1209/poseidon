package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;
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

	public static String get(String key) {
		return "";
	}

	public static Map<String, String> get(Set<String> key) {
		return null;
	}

}
