package com.gofirst.framework.authenticate;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;

/**
 * 
 * 登陆的服务
 *
 */
public interface LoginService {
	

	/**
	 * login
	 * @param userNo	用户名
	 * @param password	密码
	 * @param sessionId	sessionId
	 * @throws UnknownAccountException	无效账户
	 * @throws IncorrectCredentialsException	密码错误
	 * @throws LockedAccountException	账户锁定
	 */
	public Subject login(String userNo, String password, String type) throws UnknownAccountException, IncorrectCredentialsException, LockedAccountException;
	
}
