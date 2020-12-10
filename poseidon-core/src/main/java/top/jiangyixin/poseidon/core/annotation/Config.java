package top.jiangyixin.poseidon.core.annotation;

import java.lang.annotation.*;

/**
 * Config Annotation
 * @author jiangyixin
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {

	/**
	 * 配置key
	 * @return  key
	 */
	String key();

	/**
	 * 配置默认值
	 * @return  default value
	 */
	String defaultValue() default "";

	/**
	 * 当配置改变时是否需要刷新
	 * @return  true/false
	 */
	boolean refresh() default true;
}
