package top.jiangyixin.poseidon.admin.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jiangyixin.poseidon.admin.pojo.group.Insert;
import top.jiangyixin.poseidon.admin.pojo.group.Update;

import javax.validation.constraints.NotNull;

/**
 * Project 请求参数
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/4 下午4:08
 */
@Data
@ApiModel("Project 请求参数")
public class ProjectParam {
	
	@ApiModelProperty("主键id")
	@NotNull(groups = {Update.class}, message = "必须指定主键")
	private Long id;
	@ApiModelProperty("项目名称")
	@NotNull(groups = {Insert.class, Update.class}, message = "项目名称不能为空")
	private String name;
	@ApiModelProperty("项目 Code")
	@NotNull(groups = {Insert.class, Update.class}, message = "项目code不能为空")
	private String code;
}
