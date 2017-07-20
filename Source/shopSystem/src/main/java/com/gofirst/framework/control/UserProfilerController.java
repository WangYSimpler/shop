package com.gofirst.framework.control;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NOT_LOGIN;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.gofirst.framework.response.ResponseResult;
import com.gofirst.framework.systemService.UserProfilerService;
import com.gofirst.framework.util.Constants;
//import com.google.gson.Gson;

/**
 * 
 * 处理查询用户权限的处理器
 *
 */
@Controller
public class UserProfilerController {

	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(UserProfilerController.class);
	
	@Autowired
	private UserProfilerService userProfilerService;
	
	@RequestMapping(value="/user/profile", method = RequestMethod.GET, produces = Constants.FORMAT)
	public @ResponseBody
		String userProfile(HttpServletRequest request, HttpServletResponse response){
		ResponseResult result = new ResponseResult();
		String type = request.getParameter(Constants.TYPE);
		String sessionId = null;
		//拿到sessionId的cookie
		Cookie [] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (Constants.TOKEN.equals(cookie.getName())) {
					sessionId = cookie.getValue();
				}
			}
		}
		//调用服务userProfilerService
		String userProfilers =  userProfilerService.getProfilers(sessionId, type);
		if (userProfilers == null) {
			logger.info("未登陆");
			response.setHeader(ERROR_HEADER, ERROR_CODE_NOT_LOGIN);
			return null;
		}
		result.setData(userProfilers);
		//设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return JSON.toJSONString(result);
	}
}
