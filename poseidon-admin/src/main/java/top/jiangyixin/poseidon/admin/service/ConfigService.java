package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.context.request.async.DeferredResult;
import top.jiangyixin.poseidon.admin.pojo.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.entity.User;
import top.jiangyixin.poseidon.admin.pojo.param.ConfigParam;
import top.jiangyixin.poseidon.admin.pojo.query.ConfigQuery;
import top.jiangyixin.poseidon.admin.pojo.vo.R;

import java.util.List;

/**
 * 配置 Service 接口
 * @author jiangyixin
 * @since 2020-12-14
 */
public interface ConfigService extends IService<Config> {
	
	/**
	 * 新增配置
	 * @param configParam   configParam
	 * @param user          login user
	 * @return				R
	 */
	R<String> add(ConfigParam configParam, User user);

	/**
	 * 更新配置
	 * @param config		config
	 * @param user			login user
	 * @return				R<String>
	 */
	R<String> update(Config config, User user);

	/**
	 * 删除配置
	 * @param config		config
	 * @param user			login user
	 * @return				R<String>
	 */
	R<String> delete(Config config, User user);
	
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
	
	/**
	 * 分页查询
	 * @param page  当前页
	 * @param pageSize  每页数量
	 * @param configQueryWrapper 查询条件
	 * @return  List<Config>
	 */
	List<Config> listByPage(int page, int pageSize, QueryWrapper<Config> configQueryWrapper);
}
