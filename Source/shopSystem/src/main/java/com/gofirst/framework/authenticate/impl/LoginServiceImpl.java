package com.gofirst.framework.authenticate.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.context.ApplicationContext;

import com.gofirst.framework.authenticate.FrameworkUsernamePasswordToken;
import com.gofirst.framework.authenticate.LoginCallback;
import com.gofirst.framework.authenticate.LoginService;
import com.gofirst.framework.configure.SystemConfigProperties;
import com.gofirst.framework.session.impl.FrameworkSessionImpl;
import com.gofirst.framework.session.FrameworkSession;
import com.gofirst.framework.util.Constants;

/**
 * 
 * 登陆的服务
 *
 */
@Named
public class LoginServiceImpl implements LoginService{
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);
	
	/**
	 * spring context
	 */
	@Inject
	private ApplicationContext context;
	
	@Inject
	private SystemConfigProperties sysConfig;
	
	/**
	 * login
	 * @param userNo	用户名
	 * @param password	密码
	 * @param sessionId	sessionId
	 * @throws UnknownAccountException	无效账户
	 * @throws IncorrectCredentialsException	密码错误
	 * @throws LockedAccountException	账户锁定
	 */
	public Subject login(String userNo, String password, String type)
			throws UnknownAccountException, IncorrectCredentialsException, LockedAccountException {
		//拿到subject
		Subject subject = new Subject.Builder().authenticated(false).buildSubject();
		//如果存在session则登出
		if (subject.getSession() != null) {
			subject.logout();
		}
		FrameworkUsernamePasswordToken token = new FrameworkUsernamePasswordToken(userNo,password, "", type);
	      //认证
	      subject.login(token);
	      
	      /**
	       * 回调业务开发些的服务，使其可以对session做一些特殊处理
	       */
	      String callBackNames = sysConfig.getProperty(Constants.LOGIN_CALL_BACKS);
	      if (callBackNames == null) {
	    	  logger.info("exception : there is no property logIN.callback.names in frameworkConfig.properties");
	    	  return subject;
	      }
	      String[] callBackArray = callBackNames.split(Constants.COMMA);
	      for (String s : callBackArray) {
		      if (s != null && !Constants.EMPTY_STRING.equals(s.trim())) {
		    	  LoginCallback loginCallBack = (LoginCallback) context.getBean(s);
		    	  //得到系统的session
		    	  Session shiroSession = subject.getSession();
		    	  //把sessionId设在threadLocal中为了回调的时候能够通过权限验证
		    	  FrameworkSession.setSessionId(shiroSession.getId().toString());
		    	  //封装成框架的session
		    	  FrameworkSession session = FrameworkSessionImpl.createSession(shiroSession, userNo); 
		    	  loginCallBack.callback(session, subject);
		      }
	      }
	      
	      return subject;
	      
	    }
}
