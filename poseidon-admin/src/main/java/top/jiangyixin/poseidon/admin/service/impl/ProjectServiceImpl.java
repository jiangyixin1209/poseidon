package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.jiangyixin.poseidon.admin.pojo.entity.Project;
import top.jiangyixin.poseidon.admin.mapper.ProjectMapper;
import top.jiangyixin.poseidon.admin.pojo.param.ProjectParam;
import top.jiangyixin.poseidon.admin.pojo.vo.ProjectVo;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.ProjectService;
import top.jiangyixin.poseidon.admin.util.PojoUtils;

/**
 * 项目 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
	
	@Override
	public R<?> create(ProjectParam projectParam) {
		Project project = getOne(new QueryWrapper<Project>()
				.eq("code", projectParam.getCode()));
		if (project != null) {
			return R.fail("项目code已存在");
		}
		project = PojoUtils.copy(projectParam, Project.class);
		save(project);
		ProjectVo projectVo = PojoUtils.copy(project, ProjectVo.class);
		return new R<>(R.SUCCESS_CODE, projectVo);
	}
}
