package top.jiangyixin.poseidon.admin.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登录参数
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/30 下午5:09
 */
@Data
@ApiModel("登录请求参数")
public class LoginParam {
	
	@ApiModelProperty("用户名或邮箱")
	@NotNull(message = "用户名或邮箱不能为空")
	private String username;
	
	@ApiModelProperty("密码")
	@NotNull(message = "密码不能为空")
	private String password;
}
