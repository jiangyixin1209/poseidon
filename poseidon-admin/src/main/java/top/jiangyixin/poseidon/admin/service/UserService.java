package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.jiangyixin.poseidon.admin.pojo.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.pojo.param.LoginParam;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

/**
 * 用户 Service 接口
 * @author jiangyixin
 * @since 2020-12-14
 */
public interface UserService extends IService<User> {
	
	/**
	 * 是否有操作改项目配置的权限
	 * @param user              user
	 * @param config            config
	 * @return                  true/fase
	 */
	boolean hasProjectPermission(User user, Config config);
	
	/**
	 * 登录
	 * @param loginParam        登录参数
	 * @return                  R
	 */
	R<String> login(LoginParam loginParam);
}
