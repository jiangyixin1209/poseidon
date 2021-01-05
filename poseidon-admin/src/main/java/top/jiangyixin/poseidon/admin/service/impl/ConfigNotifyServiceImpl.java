package top.jiangyixin.poseidon.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.jiangyixin.poseidon.admin.mapper.ConfigNotifyMapper;
import top.jiangyixin.poseidon.admin.pojo.entity.ConfigNotify;
import top.jiangyixin.poseidon.admin.service.ConfigNotifyService;

/**
 * TODO
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2020/12/26
 */
@Service
public class ConfigNotifyServiceImpl extends ServiceImpl<ConfigNotifyMapper, ConfigNotify>
        implements ConfigNotifyService {
	
	@Override
	public void add(String env, String project, String key, String value) {
		ConfigNotify configNotify = new ConfigNotify();
		configNotify.setEnv(env);
		configNotify.setProject(project);
		configNotify.setKey(key);
		configNotify.setValue(value);
		save(configNotify);
	}
}
