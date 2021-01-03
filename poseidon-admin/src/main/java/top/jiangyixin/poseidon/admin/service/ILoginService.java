package top.jiangyixin.poseidon.admin.service;

import top.jiangyixin.poseidon.admin.pojo.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录Service接口
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2021/1/3
 */
public interface ILoginService {

  /**
   * 标识
   */
  String LOGIN_IDENTITY = "POSEIDON_TOKEN";

  /**
   * 检查是否已经登录
   * @param request 请求对象
   * @return 用户对象
   */
  User checkLogin(HttpServletRequest request);
}
