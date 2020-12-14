package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.jiangyixin.poseidon.admin.entity.User;
import top.jiangyixin.poseidon.admin.mapper.UserMapper;
import top.jiangyixin.poseidon.admin.service.UserService;

/**
 * 用户 Service 接口实现
 * @author jiangyixin
 * @since 2020-12-14
 */
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
