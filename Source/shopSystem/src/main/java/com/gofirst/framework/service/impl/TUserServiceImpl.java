package com.gofirst.framework.service.impl;

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
@Named(value="UserService")
public class TUserServiceImpl implements TUserService {
	
	private static final Logger logger = Logger.getLogger(TUserServiceImpl.class);
	
	@Inject
	private TUserRepository userRepository;


	/**
	 * 是否有权限登录
	 * 
	 * @param loginId
	 * @param pwd 
	 * return
	 */
	@Override
	public boolean isAuthenticatedUser(String loginId, String pwd) {
		TUser user = userRepository.findByUserNoAndFlag(loginId,'0');
		if(user == null) logger.info("can not find user" + loginId);
		logger.info(user.getUserName());
		logger.info(user.getPassword());
		if (!pwd.equals(user.getPassword())) {
			return false;
		}
		
		return true;
	}
}
