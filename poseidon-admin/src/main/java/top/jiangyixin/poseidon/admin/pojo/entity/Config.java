package top.jiangyixin.poseidon.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 配置表
 * @author jiangyixin
 * @since 2020-12-13
 */
@Data
@Builder
public class Config {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String env;
    private String project;
    private String key;
    private String value;
    private String description;
    private Date gmtCreate;
    private Date gmtModified;
}
