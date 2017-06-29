package com.gofirst.framework.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务接口 
 *
 */
public interface TUserService {

	/**
	 * 是否有权限登录
	 * @param loginId	用户名
	 * @param pwd	秘密
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public boolean isAuthenticatedUser(String loginId, String pwd, String type);
	
	/**
	 * 是否有权限登录
	 * @param loginId	用户名
	 * @param pwd	秘密
	 * @return
	 */
	/**
	 * 是否有权限登录
	 * @param loginId	用户名
	 * @param  pwd	秘密
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public boolean isAuthenticatedUser(String loginId, String pwd);
	
}
