package com.gofirst.framework.authenticate.impl;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;

import com.gofirst.framework.authenticate.LogoutService;

/**
 * 
 * 登出服务
 *
 */
@Named
public class LogoutServiceImpl implements LogoutService{

	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(LogoutService.class);

	/**
	 * 登出
	 * @param sessionId	用户的唯一标识符
	 */
	public void logout(String sessionId) {
		if (sessionId != null) {
			try {
				// 拿到subject
				Subject subject = new Subject.Builder().sessionId(sessionId)
						.buildSubject();
				// 登出
				subject.logout();
			} catch (Exception e) {
				logger.info("session无效或者过期");
			}
		}
	}
	
}
