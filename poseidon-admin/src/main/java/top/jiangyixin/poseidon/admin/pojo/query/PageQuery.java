package top.jiangyixin.poseidon.admin.pojo.query;

import lombok.Data;

/**
 * 分页查询参数
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/4 下午4:10
 */
@Data
public class PageQuery {
	/**
	 * 当前页数
	 */
	private int page;
	/**
	 * 每页限制
	 */
	private int size;
}
