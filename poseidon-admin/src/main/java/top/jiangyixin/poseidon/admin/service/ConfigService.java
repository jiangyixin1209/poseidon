package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.context.request.async.DeferredResult;
import top.jiangyixin.poseidon.admin.pojo.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.pojo.query.ConfigQuery;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

/**
 * 配置 Service 接口
 * @author jiangyixin
 * @since 2020-12-14
 */
public interface ConfigService extends IService<Config> {
	
	/**
	 * 新增配置
	 * @param config        config
	 * @param user          login user
	 * @return
	 */
	R<String> add(Config config, User user);
	
	/**
	 * 获取配置
	 * @param configQuery           configQuery
	 * @return                      R<Map<String, String>>
	 */
	R<?> get(ConfigQuery configQuery);
	
	/**
	 * 监控相关配置是否有更改
	 * @param configQuery           configQuery
	 * @return                      DeferredResult
	 */
	DeferredResult<R<String>> monitor(ConfigQuery configQuery);
}
