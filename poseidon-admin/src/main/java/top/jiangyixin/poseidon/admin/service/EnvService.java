package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.jiangyixin.poseidon.admin.pojo.entity.Env;
import top.jiangyixin.poseidon.admin.pojo.param.EnvParam;
import top.jiangyixin.poseidon.admin.pojo.vo.EnvVo;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

import java.util.List;

/**
 * Env Service 接口
 * @author jiangyixin
 * @since 2020-12-14
 */
public interface EnvService extends IService<Env> {
	
	/**
	 * 查询所有 env
	 * @return          env list
	 */
	List<EnvVo> findAll();

	/**
	 * 新增env
	 * @param envParam 新增参数
	 * @return envVO
	 */
	R<?> create(EnvParam envParam);
}
