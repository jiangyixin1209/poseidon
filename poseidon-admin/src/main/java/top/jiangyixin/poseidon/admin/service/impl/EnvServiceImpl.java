package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.jiangyixin.poseidon.admin.pojo.entity.Env;
import top.jiangyixin.poseidon.admin.mapper.EnvMapper;
import top.jiangyixin.poseidon.admin.pojo.param.EnvParam;
import top.jiangyixin.poseidon.admin.pojo.vo.EnvVo;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.EnvService;
import top.jiangyixin.poseidon.admin.util.PojoUtil;

import java.util.List;

/**
 * Env Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class EnvServiceImpl extends ServiceImpl<EnvMapper, Env> implements EnvService  {
	
	
	@Override
	public List<EnvVo> findAll() {
		List<Env> envList = this.list(new QueryWrapper<Env>().orderByDesc("sort"));
		return PojoUtil.copyList(envList, EnvVo.class);
	}

	@Override
	public R<?> create(EnvParam envParam) {
		Env env = this.getOne(new QueryWrapper<Env>().eq("code", envParam.getCode()));
		if (env != null) {
			return R.fail("code已存在");
		}
		env = PojoUtil.copy(envParam, Env.class);
		this.saveOrUpdate(env);
		EnvVo envVo = PojoUtil.copy(env, EnvVo.class);
		return new R<>(R.SUCCESS_CODE, envVo);
	}
}
