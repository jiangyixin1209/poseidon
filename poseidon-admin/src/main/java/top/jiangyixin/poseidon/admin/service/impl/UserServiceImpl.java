package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.jiangyixin.poseidon.admin.pojo.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.mapper.UserMapper;
import top.jiangyixin.poseidon.admin.service.UserService;

/**
 * 用户 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	@Override
	public boolean hasProjectPermission(User user, Config config) {
		if (user.getPermission() == 1) {
			return true;
		}
		String[] permissions = StringUtils.split(user.getPermissionData(), ",");
		return ArrayUtils.contains(permissions, config.getEnv().concat("#").concat(config.getProject()));
	}
}
