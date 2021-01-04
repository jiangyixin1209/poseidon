package top.jiangyixin.poseidon.admin.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jiangyixin.poseidon.admin.pojo.group.Insert;
import top.jiangyixin.poseidon.admin.pojo.group.Update;

import javax.validation.constraints.NotNull;

/**
 * Config 请求参数
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/4 下午4:28
 */
@Data
@ApiModel("Config Param")
public class ConfigParam {
	
	@ApiModelProperty("主键id")
	@NotNull(groups = {Update.class}, message = "必须指定主键")
	private Long id;
	
	@ApiModelProperty("所属环境")
	@NotNull(groups = {Insert.class, Update.class}, message = "所属环境不能为空")
	private String env;
	
	@ApiModelProperty("所属项目")
	@NotNull(groups = {Insert.class})
	private String project;
	private String key;
	private String value;
	private String description;
}
