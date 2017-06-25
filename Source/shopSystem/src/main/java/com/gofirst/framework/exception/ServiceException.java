package com.gofirst.framework.exception;

/**
 * 服务抛出异常给前台。
 *
 */
public class ServiceException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2521417884229743055L;
	
	/**
	 * 错误码，只能大于0
	 */
	private int errorCode;
	/**
	 * 错误信息
	 */
	private String errorMsg;
	
	public ServiceException() {
		super();
	}

	public ServiceException(int errorCode, String errorMsg) {
		super();
		if(errorCode <= 0 ) {
			throw new RuntimeException("error code cannot be less than 0");
		}
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	

}
