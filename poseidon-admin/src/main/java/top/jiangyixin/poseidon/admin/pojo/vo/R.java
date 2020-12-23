package top.jiangyixin.poseidon.admin.pojo.vo;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/23 上午11:47
 */
public class R<T> {
	
	public final static int SUCCESS_CODE = 200;
	public final static int ERROR_CODE = 500;
	
	private int code;
	private String message;
	private T data;
	
	public R() {
	}
	
	public R(int code, T data) {
		this.code = code;
		this.data = data;
	}
	
	public R(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public R(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public static R<String> success() {
		return new R<>(SUCCESS_CODE, "操作成功");
	}
	
	public static R<String> success(String message) {
		return new R<>(SUCCESS_CODE, message);
	}
	
	public static R<String> fail() {
		return new R<>(ERROR_CODE, null);
	}
	
	public static R<String> fail(String message) {
		return new R<>(ERROR_CODE, message);
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
}
