package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.jiangyixin.poseidon.admin.config.PoseidonConfig;
import top.jiangyixin.poseidon.admin.pojo.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.mapper.UserMapper;
import top.jiangyixin.poseidon.admin.pojo.param.LoginParam;
import top.jiangyixin.poseidon.admin.pojo.vo.R;
import top.jiangyixin.poseidon.admin.service.UserService;
import top.jiangyixin.poseidon.admin.util.JwtUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	
	@Resource
	PoseidonConfig poseidonConfig;
	
	@Override
	public boolean hasProjectPermission(User user, Config config) {
		if (user.getPermission() == 1) {
			return true;
		}
		String[] permissions = StringUtils.split(user.getPermissionData(), ",");
		return ArrayUtils.contains(permissions, config.getEnv().concat("#").concat(config.getProject()));
	}
	
	@Override
	public R<String> login(LoginParam loginParam) {
		// 先简单实现
		if ("admin".equals(loginParam.getUsername()) && "admin".equals(loginParam.getPassword())) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("id", "1");
			return R.success(JwtUtil.createJwt(poseidonConfig.getSecret(), "user", claims, -1));
		}
		return R.fail("登录失败");
	}
}
