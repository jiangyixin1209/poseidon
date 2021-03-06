package top.jiangyixin.poseidon.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 用户表
 * @author jiangyixin
 * @since 2020-12-13
 */
@Data
@Builder
public class User {
    /**
     * 管理员
     */
    public final static int ADMIN = 0;
    /**
     * 普通用户
     */
    public final static int NORMAL = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Integer permission;
    private String permissionData;
    private Date gmtCreate;
    private Date gmtModified;
}
