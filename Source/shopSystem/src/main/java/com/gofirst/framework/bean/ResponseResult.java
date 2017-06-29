package com.gofirst.framework.bean;

import java.io.Serializable;

/**
 * 前后台交互结果类
 * 
 */
public class ResponseResult implements Serializable {

	/**
	 * 版本
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 当前的数据版本 该位置存放系统中版本信息
	 */
	//protected final String version = "1.0-wang";

	/**
	 * 错误码，默认0为正确
	 */
	protected int errorCode = 0;

	/**
	 * 错误消息
	 */
	protected String errorMsg = null;

	/**
	 * 获取结果的json串
	 */
	protected String data = "";

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

//	public String getVersion() {
//		return version;
//	}

}
