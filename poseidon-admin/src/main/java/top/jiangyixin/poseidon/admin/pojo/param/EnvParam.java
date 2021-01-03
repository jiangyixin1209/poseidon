package top.jiangyixin.poseidon.admin.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import top.jiangyixin.poseidon.admin.pojo.group.Insert;
import top.jiangyixin.poseidon.admin.pojo.group.Update;

import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2021/1/3
 */
@ApiModel("Env Param")
@Data
public class EnvParam {

  @ApiModelProperty("主键id")
  @NotNull(groups = {Update.class}, message = "必须指定主键")
  private Long id;
  @ApiModelProperty("Env名称")
  @NotNull(groups = {Insert.class, Update.class}, message = "名称不能为空")
  private String name;
  @ApiModelProperty("Env Code")
  @NotNull(groups = {Insert.class, Update.class}, message = "code不能为空")
  private String code;
  @ApiModelProperty("排序")
  @NotNull(groups = {Insert.class, Update.class}, message = "排序不能为空")
  private Integer sort;

}
