package top.jiangyixin.poseidon.admin.pojo.query;

import lombok.Data;

import java.util.List;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/24 下午4:12
 */
@Data
public class ConfigQuery {
	
	private String accessToken;
	private String env;
	private List<String> keys;
}
