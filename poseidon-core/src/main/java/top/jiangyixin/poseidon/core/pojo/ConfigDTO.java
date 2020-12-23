package top.jiangyixin.poseidon.core.pojo;

import java.util.List;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/22 下午5:06
 */
public class ConfigDTO {
	
	public static class ConfigVO {
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return "ConfigVO{" +
					"key='" + key + '\'' +
					", value='" + value + '\'' +
					'}';
		}
	}
	
	private Integer code;
	private String message;
	private List<ConfigVO> configList;
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<ConfigVO> getConfigList() {
		return configList;
	}
	
	public void setConfigList(List<ConfigVO> configList) {
		this.configList = configList;
	}
	
	@Override
	public String toString() {
		return "ConfigDTO{" +
				"code=" + code +
				", message='" + message + '\'' +
				", configList=" + configList +
				'}';
	}
}
