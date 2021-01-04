package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.jiangyixin.poseidon.admin.pojo.entity.Project;
import top.jiangyixin.poseidon.admin.pojo.param.ProjectParam;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

/**
 * 项目 Service 接口
 * @author jiangyixin
 * @since 2020-12-14
 */
public interface ProjectService extends IService<Project> {
	
	/**
	 * 新增项目
	 * @param projectParam  请求参数
	 * @return  R
	 */
	R<?> create(ProjectParam projectParam);
}
