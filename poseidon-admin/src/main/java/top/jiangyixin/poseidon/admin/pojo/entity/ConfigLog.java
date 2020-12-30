package top.jiangyixin.poseidon.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 配置日志变更表
 * @author jiangyixin
 * @since 2020-12-13
 */
@Data
public class ConfigLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String env;
    private String project;
    private String key;
    private String description;
    private String oldValue;
    private String newValue;
    private String optUser;
    private Date gmtCreate;
    private Date gmtModified;
}
