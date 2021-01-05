package top.jiangyixin.poseidon.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
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
    @TableField(value = "`key`")
    private String key;
    private String value;
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
