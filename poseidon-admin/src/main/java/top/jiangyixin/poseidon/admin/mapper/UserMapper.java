package top.jiangyixin.poseidon.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.jiangyixin.poseidon.admin.pojo.entity.User;

/**
 * 用户表 Mapper 接口
 * @author jiangyixin
 * @since 2020-12-13
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
