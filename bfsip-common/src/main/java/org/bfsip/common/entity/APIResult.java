package org.bfsip.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bfsip.common.utils.JacksonUtil;

/** 
 * 所有服务统一返回数据的数据对象
 *
 * <pre> 
 * project: bfsip-common
 * author: eddy
 * email: xqxyxchy@126.com
 * date: 2018年3月5日-下午9:01:32
 * rights: eddy
 * </pre>
 */
public class APIResult implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 警告 */
	public static final int WARN = 1;
	/** 成功 */
	public static final int SUCCESS = 0;
	/** 失败 */
	public static final int FAIL = -1;
	/** 错误 */
	public static final int ERROR = -2;
	/** 登陆超时 */
	public static final int TIMEOUT = -3;
	
	// 返回结果(成功或失败)
	private int result = SUCCESS;
	// 返回消息
	private String message = "";
	// 引起原因
	private String cause = "";
	// 返回变量
	private Map<String, Object> variables = new HashMap<String, Object>();
	// 返回数据
	private Object data;
	
	public APIResult() {
	}

	public APIResult(int result) {
		super();
		this.result = result;
	}

	public APIResult(int result, String message) {
		this.result = result;
		this.message = message;
	}

	public APIResult(int result, String message, String cause) {
		this.result = result;
		this.message = message;
		this.cause = cause;
	}
	
	public void addVariable(String key, Object value) {
		this.variables.put(key, value);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	public boolean isSuccess(){
		return SUCCESS == result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Object getVariable(String key) {
		if(null == variables){
			return null;
		}
		return variables.get(key);
	}
	
	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> vars) {
		this.variables = vars;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toJsonString(){
		return JacksonUtil.toJsonString(this);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
	
}
