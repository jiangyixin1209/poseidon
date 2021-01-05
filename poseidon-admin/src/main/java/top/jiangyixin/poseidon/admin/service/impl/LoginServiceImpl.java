package top.jiangyixin.poseidon.admin.service.impl;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import top.jiangyixin.poseidon.admin.config.PoseidonConfig;
import top.jiangyixin.poseidon.admin.mapper.UserMapper;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.service.ILoginService;
import top.jiangyixin.poseidon.admin.util.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录Service接口实现
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2021/1/3
 */
@Service
public class LoginServiceImpl implements ILoginService {

  @Resource
  private PoseidonConfig poseidonConfig;
  @Resource
  private UserMapper userMapper;

  @Override
  public User checkLogin(HttpServletRequest request) {
    String token = request.getHeader(ILoginService.LOGIN_IDENTITY);
    if (token == null) {
      return null;
    }

    Claims claims = JwtUtils.parseJwt(poseidonConfig.getSecret(), token);
    if (claims == null) {
      return null;
    }

    Long id = Long.parseLong(claims.get("id").toString());
    return userMapper.selectById(id);
  }
}
