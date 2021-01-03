package top.jiangyixin.poseidon.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jiangyixin.poseidon.admin.annotation.Permission;
import top.jiangyixin.poseidon.admin.pojo.entity.Env;
import top.jiangyixin.poseidon.admin.pojo.param.EnvParam;
import top.jiangyixin.poseidon.admin.pojo.vo.EnvVo;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.EnvService;
import top.jiangyixin.poseidon.admin.util.PojoUtil;

import java.util.List;

/**
 * @author jiangyixin
 */
@Api(tags = "环境")
@RestController
@RequestMapping("/env")
public class EnvController {
	
	private final EnvService envService;
	
	@Autowired
	public EnvController(EnvService envService) {
		this.envService = envService;
	}

	@ApiOperation("列出所有Env")
	@GetMapping("/list")
	@Permission
	public R<List<EnvVo>> list() {
		List<EnvVo> list = envService.findAll();
		return new R<>(R.SUCCESS_CODE, list);
	}

	@ApiOperation("新增Env")
	@PostMapping("/add")
	@Permission(needAdmin = true)
	public R<?> create(@Validated EnvParam envParam){
		return envService.create(envParam);
	}

	
}
