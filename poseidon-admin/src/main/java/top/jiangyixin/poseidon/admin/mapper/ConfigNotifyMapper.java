package top.jiangyixin.poseidon.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.jiangyixin.poseidon.admin.pojo.entity.ConfigNotify;

import java.util.List;

/**
 * TODO
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2020/12/26
 */
@Repository
public interface ConfigNotifyMapper extends BaseMapper<ConfigNotify> {

    /**
     * 清除过期的config变更Notify
     * @param timeout           过期时间
     * @return                  影响行数
     */
    int cleanExpireNotify(@Param("timeout") int timeout);
    
    /**
     * 找出所有id不属于ids集合中的Notify
     * @param ids               id列表
     * @return                  List<ConfigNotify>
     */
    List<ConfigNotify> selectAllNotInIdList(@Param("ids") List<Long> ids);
}
