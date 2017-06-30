package com.gofirst.framework.interceptor;

import static com.gofirst.framework.util.Constants.SLASH;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NOT_LOGIN;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gofirst.framework.configure.SystemConfigProperties;
import com.gofirst.framework.session.FrameworkSession;
import com.gofirst.framework.session.NoSession;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;

/**
 * 会话过滤
 * 
 */
@Named
@Singleton
public class SessionInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger(SessionInterceptor.class);

	/**
	 * 定义不需要身份的访问
	 */
	private static Pattern urlPattern = Pattern.compile("^/login|^/logout");

	/**
	 * system config
	 */
	@Inject
	private SystemConfigProperties sysConfig = null;

	@Autowired(required = false)
	private NoSession noSessionService = null;

	/**
	 * spring context
	 */
	@Autowired(required = true)
	private ApplicationContext context;

	@PostConstruct
	public void init() {
		// 当前访问的是否为sso中或配置中不需要会话的
		// 没有引入sso，从配置文件中读出不需要会话的访问
		String nosessionPermissions = sysConfig.getProperty(Constants.NOSESSIONPERMISSIONS);
		if (nosessionPermissions == null || nosessionPermissions.trim().equals(Constants.EMPTY_STRING)) {
			if (noSessionService != null) {// 从sso中读出不需要会话的访问
				Helper.nosessions = noSessionService.getNoSessions(sysConfig.getProperty(Constants.SYSTEMNAME));
			}
		} else {
			Helper.nosessions = nosessionPermissions.split(Constants.POUND);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.
	 * servlet .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.info("request url :" + request.getServletPath() + " " + request.getMethod() + " device_uuid:"
				+ request.getHeader("device_uuid") + " device_os:" + request.getHeader("device_os") + " app_version:"
				+ request.getHeader("app_version"));
		// 记录请求的参数除了login, logout的请求
		if (!urlPattern.matcher(request.getServletPath()).find()) {
			Map<String, String[]> parameterMap = request.getParameterMap();
			for (String key : parameterMap.keySet()) {
				logger.info("parameter key : " + key);
				for (String value : parameterMap.get(key)) {
					logger.info("parameter value : " + value);
				}
			}
		}

		StringJoiner sj = new StringJoiner(",");
		sj.add(request.getRemoteHost());
		sj.add(request.getRemoteAddr());
		sj.add(String.valueOf(request.getRemotePort()));
		logger.info("remote :" + sj.toString());

		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			logger.info("http request header: " + header + ":" + request.getHeader(header));
		}

		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("text/plain;charset=UTF-8");

		// 是否当前访问需要会话，如果当前访问不需要会话
		if (urlPattern.matcher(request.getServletPath()).find() || isNoSession(request)) {
			logger.info("url requires no session " + urlPattern);
			return true;
		}

		String sessionId = null;
		// 拿到sessionId的cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (Constants.TOKEN.equals(cookie.getName())) {
					sessionId = cookie.getValue();
				}
			}
		}
		Subject subject = new Subject.Builder().sessionId(sessionId).buildSubject();
		// 检验用户是否已经登陆
		if (!subject.isAuthenticated()) {
			response.setHeader(ERROR_HEADER, ERROR_CODE_NOT_LOGIN);
			return false;
		}
		Session session = subject.getSession();
		// 更新session
		session.touch();

		// 把sessionId设在threadLocal中
		FrameworkSession.setSessionId(sessionId);

		// 得到过期时间
		long configTimeout = session.getTimeout();
		long timeout = System.currentTimeMillis() + configTimeout;
		// 过期时间格式转换为8+6格式
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
		Date date = new Date(timeout);
		String chinaTimeout = df.format(date);

		// 设置cookie expire_time
		Cookie cookieExpireTime = new Cookie(Constants.EXPIRE_TIME, chinaTimeout);
		cookieExpireTime.setPath(SLASH);

		response.addCookie(cookieExpireTime);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.
	 * servlet .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		FrameworkSession.removeSessionId();
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("request url :" + request.getServletPath() + " " + request.getMethod() + " returned");
		return;
	}

	private boolean isNoSession(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		// 访问user/profile需要认证
		if (servletPath.startsWith("/user/profile")) {
			return false;
		}
		// 把url中拿到类名方法名
		String[] pathArray = servletPath.split(Constants.SLASH);
		// 检查请求类型，repository只允许get方法可以不用认证
		if (pathArray[1].equals("repository")) {
			if (!request.getMethod().equals(RequestMethod.GET.toString())) {
				return false;
			}
		}
		String className;
		String methodName;
		if ("key".equals(pathArray[2])) {
			className = pathArray[3];
			methodName = pathArray[4];
		} else {
			className = pathArray[2];
			methodName = pathArray[3];
		}

		return Helper.isNosession(context.getBean(className), methodName);
	}
}
