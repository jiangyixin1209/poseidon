package top.jiangyixin.poseidon.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 项目表
 * @author jiangyixin
 * @since 2020-12-13
 */
@Data
@Builder
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Date gmtCreate;
    private Date gmtModified;
}
