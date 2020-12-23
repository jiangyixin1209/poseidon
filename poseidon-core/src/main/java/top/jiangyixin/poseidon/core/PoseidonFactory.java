package top.jiangyixin.poseidon.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import top.jiangyixin.poseidon.core.core.LocalCacheConfig;
import top.jiangyixin.poseidon.core.core.MirrorConfig;
import top.jiangyixin.poseidon.core.core.RemoteConfig;
import top.jiangyixin.poseidon.core.listener.PoseidonListenerManager;
import top.jiangyixin.poseidon.core.listener.impl.RefreshPoseidonListener;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/22 下午4:34
 */
public class PoseidonFactory extends InstantiationAwareBeanPostProcessorAdapter implements
		InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware {
	
	private String address;
	private String env;
	private String accessToken;
	private String mirrorFile;
	private String beanName;
	private static BeanFactory beanFactory;
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setEnv(String env) {
		this.env = env;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void setMirrorFile(String mirrorFile) {
		this.mirrorFile = mirrorFile;
	}
	
	
	/**
	 * 初始化
	 * @param address
	 * @param env
	 * @param accessToken
	 * @param mirrorFile
	 */
	public static void init(String address, String env, String accessToken, String mirrorFile) {
		RemoteConfig.init(address, env, accessToken);
		MirrorConfig.init(mirrorFile);
		LocalCacheConfig.init();
		PoseidonListenerManager.addListener(null, new RefreshPoseidonListener());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init(address, env, accessToken, mirrorFile);
	}
	
	/**
	 * 销毁
	 */
	@Override
	public void destroy() throws Exception {
		LocalCacheConfig.destroy();
	}
	
	/**
	 * 实现BeanNameAware接口
	 * @param name              beanName
	 */
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}
	
	/**
	 * 实现BeanFactoryAware接口
	 * @param beanFactory           beanFactory
	 * @throws BeansException       BeansException
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		PoseidonFactory.beanFactory = beanFactory;
	}
	
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return super.postProcessAfterInstantiation(bean, beanName);
	}
}
