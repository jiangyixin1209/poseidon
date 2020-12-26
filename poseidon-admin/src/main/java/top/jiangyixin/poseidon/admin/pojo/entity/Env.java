package top.jiangyixin.poseidon.admin.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 环境表
 * @author jiangyixin
 * @since 2020-12-13
 */
@Data
@TableName("poseidon_env")
public class Env {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Integer sort;
    private Date gmtCreate;
    private Date gmtModified;
}
