package top.jiangyixin.poseidon.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.tools.jconsole.Tab;
import top.jiangyixin.poseidon.admin.annotation.Permission;
import top.jiangyixin.poseidon.admin.pojo.param.ProjectParam;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.ProjectService;

import javax.annotation.Resource;

/**
 * @author jiangyixin
 */
@Api(tags = "项目")
@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Resource
	private ProjectService projectService;
	
	@ApiOperation("新增项目")
	@PostMapping("/add")
	@Permission(needAdmin = true)
	public R<?> create(@Validated @RequestBody ProjectParam projectParam) {
		return projectService.create(projectParam);
	}
}
