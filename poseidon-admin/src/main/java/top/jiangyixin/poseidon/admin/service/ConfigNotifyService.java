package top.jiangyixin.poseidon.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.jiangyixin.poseidon.admin.pojo.entity.ConfigNotify;

/**
 * TODO
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2020/12/26
 */
public interface ConfigNotifyService extends IService<ConfigNotify> {
	
	/**
	 * 新增config notify
	 * @param env   env
	 * @param project   project
	 * @param key   key
	 * @param value value
	 */
	void add(String env, String project, String key, String value);
}
