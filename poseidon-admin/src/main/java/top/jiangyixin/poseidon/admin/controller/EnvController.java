package top.jiangyixin.poseidon.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jiangyixin.poseidon.admin.pojo.vo.EnvVo;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.EnvService;

import java.util.List;

/**
 * @author jiangyixin
 */
@RestController
@RequestMapping("/env")
public class EnvController {
	
	private final EnvService envService;
	
	@Autowired
	public EnvController(EnvService envService) {
		this.envService = envService;
	}
	
	@GetMapping("/list")
	public R<List<EnvVo>> list() {
		List<EnvVo> list = envService.findAll();
		return new R<>(R.SUCCESS_CODE, list);
	}
	
}
