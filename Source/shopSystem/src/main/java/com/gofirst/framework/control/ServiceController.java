package com.gofirst.framework.control;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM_VALID;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.systemService.ServiceHandler;
import com.gofirst.framework.util.Constants;
//import com.google.gson.Gson;

/**
 * 通用服务处理器
 * 根据服务名，方法名转发到相应的服务.
 */
@Controller
public class ServiceController {
	
	/**
	 * spring context
	 */
	@SuppressWarnings("unused")
	@Autowired(required = true)
	private ApplicationContext context;
	
	
	/**
	 * gson
	 */
	//@SuppressWarnings("unused")
	//@Autowired(required = true)
	//private Gson gson = null;
	
	/**
	 * java validator
	 */
	@SuppressWarnings("unused")
	@Autowired
	private Validator validator;
	
	@Autowired
	private ServiceHandler serviceHandler;
	
	
	/**
	 * 处理service的请求. 参数params=[]
	 * @param request http request
	 * @param response	http response
	 * @param serviceName	服务名
	 * @param funcName	方法名
	 * @return	response body
	 * @throws BeansException
	 * @throws JSONException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws Exception
	 */
	@RequestMapping(value="/service/{serviceName}/{funcName}", method = RequestMethod.POST, produces = Constants.FORMAT)
	public @ResponseBody
		String doService(HttpServletRequest request,HttpServletResponse response,
						@PathVariable String serviceName, @PathVariable String funcName) 
								throws BeansException,  JSONException, IllegalAccessException, 
								IllegalArgumentException,InvocationTargetException, Exception {
		String params = null;
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.get(Constants.PARAMS) != null) {
			params = paramMap.get(Constants.PARAMS)[0];
		}
		String result = serviceHandler.handleService(serviceName, funcName, params);
		if (ERROR_CODE_PARAM_VALID.equals(result) || ERROR_CODE_NO_SERVICE.equals(result)) {
			response.setHeader(ERROR_HEADER, result);
			return null;
		}
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		// 转换成json返回
		return result;
	}
	
	/**
	 * 处理service的请求. 参数key=value&k1=v1
	 * @param request http request
	 * @param response	http response
	 * @param serviceName	服务名
	 * @param funcName	方法名
	 * @return	response body
	 * @throws BeansException
	 * @throws JSONException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws Exception
	 */
	@RequestMapping(value="/service/key/{serviceName}/{funcName}", method = RequestMethod.POST, produces = Constants.FORMAT)
	public @ResponseBody
		String doKeyService(HttpServletRequest request,HttpServletResponse response,
						@PathVariable String serviceName, @PathVariable String funcName) 
								throws BeansException,  JSONException, IllegalAccessException, 
								IllegalArgumentException,InvocationTargetException, Exception {
		String result = serviceHandler.handleService(serviceName, funcName, request.getParameterMap());
		if (ERROR_CODE_PARAM_VALID.equals(result) || ERROR_CODE_NO_SERVICE.equals(result)) {
			response.setHeader(ERROR_HEADER, result);
			return null;
		}
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return result;
	}
	
	

}
