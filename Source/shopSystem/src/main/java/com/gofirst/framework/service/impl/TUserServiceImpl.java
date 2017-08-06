package com.gofirst.framework.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.gofirst.framework.bean.TUser;
import com.gofirst.framework.dao.TUserRepository;
import com.gofirst.framework.service.TUserService;



/**
 * 用户服务
 *
 */
@Named(value="TUserService")
public class TUserServiceImpl implements TUserService {
	
	private static final Logger logger = Logger.getLogger(TUserServiceImpl.class);
	
	@Inject
	private TUserRepository userRepository;


	/**
	 * 是否有权限登录
	 * 
	 * @param loginId
	 *            用户名
	 * @param pwd
	 *            秘密
	 * @return
	 */
	@Override
	public boolean isAuthenticatedUser(String loginId, String pwd, String type) {
		TUser user= userRepository.findByUserNo(loginId);
		
		if (!pwd.equals(user.getPassword())) {
			return false;
		}
		Set<TUser> users = new HashSet<TUser>();
		users.add(user);
		return false;
	}
	@Override
	public boolean isAuthenticatedUser(String loginId, String pwd) {
		TUser user = userRepository.findByUserNo(loginId);
		if(user == null) logger.info("can not find user" + loginId);
		logger.info(user.getUserName());
		logger.info(user.getPassword());
		if (!pwd.equals(user.getPassword())) {
			return false;
		}
		
		return true;
	}
}
