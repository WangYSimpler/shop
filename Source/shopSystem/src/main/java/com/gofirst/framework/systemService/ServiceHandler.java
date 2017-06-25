package com.gofirst.framework.systemService;

import java.lang.reflect.InvocationTargetException;

import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.exception.ServiceException;

/**
 * 增，删，改的repository服务
 *
 */
public interface ServiceHandler {

	
	public String handleService(String serviceName, String funcName, Object props) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, PermissionException, ServiceException, JSONException; 
	
}
