package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.context.request.async.DeferredResult;
import top.jiangyixin.poseidon.admin.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.query.ConfigQuery;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

import java.util.Map;

/**
 * 配置 Service 接口
 * @author jiangyixin
 * @since 2020-12-14
 */
public interface ConfigService extends IService<Config> {
	
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
