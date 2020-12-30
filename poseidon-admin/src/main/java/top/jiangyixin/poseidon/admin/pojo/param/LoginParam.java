package top.jiangyixin.poseidon.admin.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/30 下午5:09
 */
@Data
@ApiModel("登录请求参数")
public class LoginParam {
	
	@ApiModelProperty("用户名或邮箱")
	private String username;
	@ApiModelProperty("密码")
	private String password;
}
