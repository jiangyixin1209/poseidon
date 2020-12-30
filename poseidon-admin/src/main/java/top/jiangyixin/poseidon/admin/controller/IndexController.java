package top.jiangyixin.poseidon.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jiangyixin.poseidon.admin.pojo.param.LoginParam;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/30 下午5:05
 */
@Api(tags = "共用接口")
@RestController
@RequestMapping
public class IndexController {
	
	@PostMapping("/login")
	@ApiOperation("登录接口")
	public R login(@RequestBody LoginParam loginParam) {
	
	}
}
