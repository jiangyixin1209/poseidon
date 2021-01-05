package top.jiangyixin.poseidon.admin.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.jiangyixin.poseidon.admin.constant.Constant;
import top.jiangyixin.poseidon.admin.mapper.ConfigNotifyMapper;
import top.jiangyixin.poseidon.admin.pojo.entity.Config;
import top.jiangyixin.poseidon.admin.pojo.entity.ConfigNotify;
import top.jiangyixin.poseidon.admin.service.ConfigService;
import top.jiangyixin.poseidon.admin.service.impl.ConfigServiceImpl;
import top.jiangyixin.poseidon.admin.util.FileUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/5 下午2:41
 */
@Component
public class ScheduleWatchTask {
	
	private final static Logger logger = LoggerFactory.getLogger(ScheduleWatchTask.class);
	
	@Resource
	private ConfigNotifyMapper configNotifyMapper;
	@Resource
	private ConfigServiceImpl configService;
	
	/**
	 *
	 */
	@Scheduled(cron="*/1 * * * * ?")
	private void scheduleWatchConfigNotify(){
		try {
			List<Long> readNotifyIdList = configService.getReadNotifyIdList();
			List<ConfigNotify> notifyList = configNotifyMapper.selectAllNotInIdList(readNotifyIdList);
			if (notifyList != null && notifyList.size() > 0) {
				for (ConfigNotify notify : notifyList) {
					readNotifyIdList.add(notify.getId());
					// sync local file
					configService.writeConfigToFile(notify.getEnv(), notify.getProject(), notify.getKey(),notify.getValue());
				}
			}
			// 清除旧的configNotify
			if ((System.currentTimeMillis() / 1000) % Constant.DEFAULT_CLEAN_TIME == 0) {
				configNotifyMapper.cleanExpireNotify(Constant.DEFAULT_CLEAN_TIME);
				readNotifyIdList.clear();
			}
		} catch (Exception e) {
			logger.error("scheduleWatchConfigNotify: {}", e.getMessage());
		}
	}
	
	/**
	 * 同步全量的 config-data，缓存本地文件
	 * 清除被删除的config的 config-data file
	 */
	@Scheduled(cron="*/30 * * * * ?")
	private void schedulePersistConfig() {
		try {
			List<String> configDataFileList = new ArrayList<>();
			int page = 1;
			int pageSize = 1000;
			List<Config> configs = configService.listByPage(page, pageSize, null);
			while (configs != null && configs.size() > 0) {
				for (Config config : configs) {
					String configDataFile = configService.writeConfigToFile(config.getEnv(), config.getProject(),
							config.getKey(), config.getValue());
					configDataFileList.add(configDataFile);
				}
				page += 1;
				configs = configService.listByPage(page, pageSize, null);
			}
			FileUtils.cleanConfigFile(new File(configService.getConfigDir()), configDataFileList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
