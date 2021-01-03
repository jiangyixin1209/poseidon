package top.jiangyixin.poseidon.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.jiangyixin.poseidon.admin.web.interceptor.PermissionInterceptor;

import javax.annotation.Resource;

/**
 * WebMvc 配置文件
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2021/1/3
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

  @Resource
  PermissionInterceptor permissionInterceptor;


  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
    super.addInterceptors(registry);
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations(
        "classpath:/static/");
    registry.addResourceHandler("swagger-ui.html").addResourceLocations(
        "classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations(
        "classpath:/META-INF/resources/webjars/");
    super.addResourceHandlers(registry);
  }
}
