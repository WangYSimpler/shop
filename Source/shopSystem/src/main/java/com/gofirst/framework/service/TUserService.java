package com.gofirst.framework.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务接口 
 *
 */
public interface TUserService {


	/**
	 * 判断用户是否该用户
	 * @param userNo 用户编号
	 * @param pwd 密码
	 * @param type 用户类型
	 * @return
	 */
	//@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	//public boolean isAuthenticatedUser(String userNo, String pwd, String type);
	
	/**
	 * 用户密码判定
	* @param userNoStr 用户编号
	 * @param pwdStr 密码
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public boolean isAuthenticatedUser(String userNoStr, String pwdStr);
	
}
