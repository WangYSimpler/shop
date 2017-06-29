package com.gofirst.framework.control;

import static com.gofirst.framework.util.Constants.SLASH;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.math.BigInteger;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.*;
import com.gofirst.framework.authenticate.LoginService;
import com.gofirst.framework.authenticate.LogoutService;
import com.gofirst.framework.bean.ResponseResult;
import com.gofirst.framework.util.CAUtil;
import com.gofirst.framework.util.Constants;
//import com.google.gson.Gson;

/**
 * 处理登陆的controller
 *
 */

// 标注成为Spring MVC的Controller
@Controller
public class LogController {

	private static final Logger logger = Logger.getLogger(LogController.class);

	//@Autowired(required = true)
	//private Gson gson = null;

	/**
	 * wangY 20170323
	 * 登录 之后处理
	 */
	@Autowired
	private LoginService loginService;

	 /**
	  * 处理请求地址映射，负责处理"/login"(value 请求实际地址，methon 请求类型 produce
	  * request请求头中(Accept)指定类型)*/
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = Constants.FORMAT)
	public @ResponseBody String login(HttpServletRequest request, HttpServletResponse response) throws JSONException {
		ResponseResult result = new ResponseResult();
		String params = null;
		///本地表中用户编号
		String userNo = null;
		String password = null;
		String type = null;

		try {
			// 判断是否为安全登录.
			/* wangyong 20170531 删除安全登录
			 * if (request.isSecure()) {
				String certificateNum = sslLogin(request, response);
				// 把证书号做为用户名传给realm，密码为空，因为证书认证不需要密码
				handleLogin(certificateNum, "", type, response);

			} else {
				Map<String, String[]> paramMap = request.getParameterMap();
				if (paramMap.get(Constants.PARAMS) != null) {
					params = paramMap.get(Constants.PARAMS)[0];
				}
				if (params == null) {
					throw new AuthenticationException();
				}
				JSONObject jsonObject = new JSONObject(params);
				// 拿到用户名密码
				userNo = jsonObject.getString(Constants.USERNO);
				password = jsonObject.getString(Constants.PASSWORD);
				
				///是否传递用户类型过来
				if (!jsonObject.isNull(Constants.TYPE)) {
					type = jsonObject.getString(Constants.TYPE);
				}
				this.handleLogin(userNo, password, type, response);
			}*/
			
			{
				Map<String, String[]> paramMap = request.getParameterMap();
				if (paramMap.get(Constants.PARAMS) != null) {
					params = paramMap.get(Constants.PARAMS)[0];
				}
				if (params == null) {
					throw new AuthenticationException();
				}
				
				JSONObject jsonObject = JSON.parseObject(params);
				
				// 拿到用户名密码
				userNo = jsonObject.getString(Constants.USERNO);
				password = jsonObject.getString(Constants.PASSWORD);
				
				///是否传递用户类型过来
				if (!jsonObject.containsKey(Constants.TYPE)) {
					type = jsonObject.getString(Constants.TYPE);
				}
				this.handleLogin(userNo, password, type, response);
			}
		} catch (IncorrectCredentialsException | UnknownAccountException e) {
			logger.info("用户名或密码错误 ", e);
			result.setErrorCode(1);
			result.setErrorMsg("用户名或密码错误");
		} catch (LockedAccountException e) {
			logger.info("账户锁定 ", e);
			result.setErrorCode(2);
			result.setErrorMsg("账户锁定");
		} catch (ConcurrentAccessException e) {
			logger.info("用户已在别处登陆 ", e);
			result.setErrorCode(3);
			result.setErrorMsg("用户已在别处登陆");
		} catch (ExcessiveAttemptsException e) {
			logger.info("输入错误次数超过系统允许最大次数 ", e);
			result.setErrorCode(4);
			result.setErrorMsg("输入错误次数超过系统允许最大次数");
		} catch (ExpiredCredentialsException e) {
			logger.info("密码已过期 ", e);
			result.setErrorCode(5);
			result.setErrorMsg("密码已过期");
		} catch (AuthenticationException e) {
			logger.info("用户名或密码错误 ", e);
			result.setErrorCode(1);
			result.setErrorMsg("用户名或密码错误");
		} catch (Exception e) {
			logger.info("其他错误 : ", e);
			result.setErrorCode(6);
			result.setErrorMsg("其他错误");
		}

		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		//return gson.toJson(result);
		return JSON.toJSONString(result);

	}

	/**
	 * 处理登陆，返回cookie
	 * 
	 * @param userNo
	 * @param password
	 * @param type
	 * @param response
	 */
	private void handleLogin(String userNo, String password, String type, HttpServletResponse response)
			throws Exception {
		// 调用login的服务
		Subject subject = loginService.login(userNo, password, type);
		// 拿到session
		Session session = subject.getSession();
		String sessionId = session.getId().toString();
		// 得到过期时间
		long configTimeout = session.getTimeout();
		long timeout = System.currentTimeMillis() + configTimeout;
		// 过期时间格式转换为8+6格式
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
		Date date = new Date(timeout);
		String chinaTimeout = df.format(date);
		// 设置cookie token
		Cookie cookieToken = new Cookie(Constants.TOKEN, sessionId);
		cookieToken.setMaxAge(-1);
		cookieToken.setPath(SLASH);
		response.addCookie(cookieToken);
		// 设置cookie expire_time
		Cookie cookieExpireTime = new Cookie(Constants.EXPIRE_TIME, chinaTimeout);
		cookieExpireTime.setPath(SLASH);
		response.addCookie(cookieExpireTime);
		logger.info("用户 : " + userNo + " 登陆成功。 sessionId : " + sessionId);
	}

	/**
	 * 验证证书的登陆
	 * 
	 * @param request
	 * @param response
	 * @return 证书序列号
	 */
	@SuppressWarnings("unused")
	private String sslLogin(HttpServletRequest request, HttpServletResponse response) {
		X509Certificate cert = CAUtil.getCertificate(request);
		String serialStr = "";
		// 判断是否存在安全证书.如果无证书信息将跳过处理
		if (null != cert) {
			try {
				cert.checkValidity();
			} catch (CertificateExpiredException e) {
				logger.info("certificate was expired");
				throw new AuthenticationException("该证书已经过期!");
			} catch (CertificateNotYetValidException e1) {
				logger.info("certificate is not valide");
				throw new AuthenticationException("该证书还不可使用!");
			}
			BigInteger serialNumber = cert.getSerialNumber();
			serialStr = serialNumber.toString(16);

			// 判断转换成16进制的证书序列号不是负数， 并且位数不足32位， 则需要在前面填充0，
			while (serialStr.charAt(0) != '-' && serialStr.length() < 32) {
				serialStr = '0' + serialStr;
			}

			return serialStr;
		}
		return serialStr;
	}
	
	
	/**
	 * wangY 20170323
	 * 登出之后处理
	 */
	@Autowired
	private LogoutService logoutService;

	@RequestMapping(value = "/logout", method = RequestMethod.DELETE, produces = Constants.FORMAT)
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = null;
		// 拿到sessionId的cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (Constants.TOKEN.equals(cookie.getName())) {
					// 将名为token的cookie设为过期
					sessionId = cookie.getValue();
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		// 调用logout的服务
		logoutService.logout(sessionId);

		logger.info("用户登出成功。sessionId : " + sessionId);
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);

	}
}
