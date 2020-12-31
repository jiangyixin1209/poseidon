package top.jiangyixin.poseidon.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/31 下午4:43
 */
@Configuration
public class PoseidonConfig {
	
	@Value("poseidon.secret")
	private String secret;
	
	public String getSecret() {
		return secret;
	}
}
