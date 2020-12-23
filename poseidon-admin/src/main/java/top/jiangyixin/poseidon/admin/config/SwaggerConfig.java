package top.jiangyixin.poseidon.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/23 下午2:46
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.pathMapping("/")
				.select()
				.apis(RequestHandlerSelectors.basePackage("top.jiangyixin.poseidon.admin"))
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder()
						.title("Poseidon")
						.description("Poseidon分布式配置中心")
						.version("1.0")
						.contact(new Contact("jiangyixin", "jiangyixin.top", "17602173915@163.com"))
						.build());
	}
}
