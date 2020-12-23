package top.jiangyixin.poseidon.admin.pojo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/23 下午2:44
 */
@Data
@Builder
public class EnvDTO {
	
	private String code;
	private String name;
	private Integer sort;
	
}
