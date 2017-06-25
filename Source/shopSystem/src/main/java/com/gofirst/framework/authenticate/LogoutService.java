package com.gofirst.framework.authenticate;


/**
 * 
 * 登出服务
 *
 */
public interface LogoutService {
	
	/**
	 * 登出
	 * @param sessionId	用户的唯一标识符
	 */
	public void logout(String sessionId);
	
}
