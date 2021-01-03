package top.jiangyixin.poseidon.admin.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 环境表
 * @author jiangyixin
 * @since 2020-12-13
 */
@Data
public class Env {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Integer sort;
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
