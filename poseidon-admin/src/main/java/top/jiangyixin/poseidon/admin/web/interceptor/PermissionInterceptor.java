package top.jiangyixin.poseidon.admin.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.jiangyixin.poseidon.admin.annotation.Permission;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.service.ILoginService;
import top.jiangyixin.poseidon.core.exception.PoseidonException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2021/1/3
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

  @Resource
  ILoginService loginService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      return super.preHandle(request, response, handler);
    }

    HandlerMethod method = (HandlerMethod) handler;
    Permission permission = method.getMethodAnnotation(Permission.class);

    boolean needLogin = true;
    boolean needAdmin = false;
    if (permission == null) {
      return super.preHandle(request, response, handler);
    }
    needLogin = permission.needLogin();
    needAdmin = permission.needAdmin();
    if (needLogin) {
      User user = loginService.checkLogin(request);
      if (user == null) {
        throw new PoseidonException("用户未登录，拒绝请求");
      }
      if (needAdmin && user.getPermission() != User.ADMIN) {
        throw new PoseidonException("权限不足，拒绝请求");
      }
      request.setAttribute(ILoginService.LOGIN_IDENTITY, user);
    }
    return super.preHandle(request, response, handler);
  }
}
