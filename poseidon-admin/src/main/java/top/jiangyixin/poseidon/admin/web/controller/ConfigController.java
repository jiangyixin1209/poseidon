package top.jiangyixin.poseidon.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jiangyixin.poseidon.admin.annotation.Permission;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.pojo.param.ConfigParam;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.ConfigService;
import top.jiangyixin.poseidon.admin.service.ILoginService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jiangyixin
 */
@Api(tags = "配置")
@RestController
@RequestMapping("/conf")
public class ConfigController {
	
	@Resource
	private ConfigService configService;
	
	@ApiOperation("新增配置")
	@PostMapping("/create")
	@Permission
	public R<?> create(HttpServletRequest request, @Validated @RequestBody ConfigParam configParam) {
		User user = (User) request.getAttribute(ILoginService.LOGIN_IDENTITY);
		return configService.add(configParam, user);
	}
	
}
