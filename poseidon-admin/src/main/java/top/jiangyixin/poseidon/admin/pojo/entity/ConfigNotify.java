package top.jiangyixin.poseidon.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 配置通知
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2020/12/26
 */
@Data
public class ConfigNotify {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String env;
    private String project;
    private String key;
    private String value;
    private Date gmtCreate;
    private Date gmtModified;
}
