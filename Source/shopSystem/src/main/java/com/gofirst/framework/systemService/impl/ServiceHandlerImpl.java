package com.gofirst.framework.systemService.impl;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM_VALID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.apache.log4j.Logger;
import org.apache.shiro.realm.AuthorizingRealm;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.bean.ResponseResult;
import com.gofirst.framework.configure.SystemConfigProperties;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.exception.ServiceException;
import com.gofirst.framework.systemService.ServiceHandler;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;
//import com.google.gson.Gson;

@Named
public class ServiceHandlerImpl implements ServiceHandler {
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger
			.getLogger(ServiceHandlerImpl.class);
	
	/**
	 * spring context
	 */
	@Inject
	private ApplicationContext context;
	
	@Inject
	private SystemConfigProperties sysConfig = null;
	
	/**
	 * gson
	 */
	//@Inject
	//private Gson gson = null;
	
	/**
	 * shiro realm
	 */
	private AuthorizingRealm frameworkRealm = null;
	
	/**
	 * java validator
	 */
	@Inject
	private Validator validator;
	
	@PostConstruct
	public void init() {
		//根据配置文件拿到对应的realm
		frameworkRealm = (AuthorizingRealm) context.getBean(sysConfig.getProperty(Constants.REALM_BEAN_NAME));
	}

	@Override
	public String handleService(String serviceName, String funcName,Object props)
			throws IllegalAccessException,IllegalArgumentException, InvocationTargetException,PermissionException, ServiceException, JSONException {
		ResponseResult result = new ResponseResult();
		// 得到对应service名字的服务
		Object service = context.getBean(serviceName);
		// 根据方法名字反射到接口中的方法
		List<Method> methodList = Helper.getAllInterfaceMethods(service);
		boolean exist = false;
		for (Method method : methodList) {
			if (!funcName.equals(method.getName())) {
				continue;
			}
			// 判断此方法和类是否为外部可以访问
			if (!Helper.hasGatewayAnnotation(service)) {
				logger.info("there is no gateway annotation : " + serviceName);
				throw new PermissionException();
			}
			// 外部可访问的则做权限判断
			if (!Helper.hasPermission(service, method, frameworkRealm,
					Constants.SERVICE, logger)) {
				throw new PermissionException("没有访问方法的权限");
			}
			exist = true;
			JSONArray array = null;
			Object[] objects = null;
			int paramCountFromReq = 0;
			int num = 0;
			if (props == null) {
				// 判断方法含有@sessionAttribute的参数个数
				int sessionAttributeAnnotationCount = Helper
						.getSessionAttributeAnnotationCount(method);
				num += sessionAttributeAnnotationCount;
				// 方法参数个数和前台传过来参数个数相同
				if (num != method.getParameterCount()) {
					continue;
				}
				// 根据方法的参数类型，将json转换成java对象
				objects = Helper.json2ObjectArray(array, method,
						paramCountFromReq, null,
						sessionAttributeAnnotationCount > 0 ? true : false);
			}else if (props instanceof String) {// 传入参数是数组，前台按照参数位置来设置
				array = new JSONArray().fluentAdd(props);
				num = paramCountFromReq = array.size();
				// 判断方法含有@sessionAttribute的参数个数
				int sessionAttributeAnnotationCount = Helper
						.getSessionAttributeAnnotationCount(method);
				num += sessionAttributeAnnotationCount;
				// 方法参数个数和前台传过来参数个数相同
				if (num != method.getParameterCount()) {
					continue;
				}
				// 根据方法的参数类型，将json转换成java对象
				objects = Helper.json2ObjectArray(array, method,
						paramCountFromReq, null,
						sessionAttributeAnnotationCount > 0 ? true : false);
			} else if (props instanceof Map) {// 传入参数为根据参数名的key,value形式，前台按照参数名字来设值
				@SuppressWarnings("rawtypes")
				Map propMap = (Map) props;
				Parameter[] parameters = method.getParameters();
				objects = new Object[parameters.length];
				int index = -1;
				for (Parameter p : parameters) {
					index++;
					String paramName = p.getName();
					if (propMap.get(paramName) == null) {
						objects[index] = null;
						continue;
					}
					// 因为request.getParameterMap()返回的时数组，而我们只允许一一对应key-value
					Object[] values = (Object[]) propMap.get(paramName);
					Object value = values[0];
					//objects[index] = gson.fromJson((String) value, p.getType());
					objects[index] = JSON.parseObject((String) value, p.getType());
				}
			} else {
				throw new ServiceException(1, "request parameter type is wrong");
			}
			// 验证方法属性的正确性
			Class<?>[] interfacesClass = service.getClass().getInterfaces();
			boolean isRemoteService = false;
			// 是否dubbo远程调用
			for (Class<?> c : interfacesClass) {
				if (c.getName().contains("com.alibaba.dubbo")) {
					isRemoteService = true;
					break;
				}
			}
			if (!isRemoteService && objects != null) {
				ExecutableValidator executableValidtor = validator
						.forExecutables();
				Set<ConstraintViolation<Object>> constraintViolations = executableValidtor
						.validateParameters(service, method, objects);
				if (constraintViolations.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for (ConstraintViolation<Object> cons : constraintViolations) {
						sb.append(cons.getPropertyPath().toString());
						sb.append(cons.getMessage());
					}
					logger.info("validation failed : " + sb.toString());
					return ERROR_CODE_PARAM_VALID;
				}
			}
			// 反射调用方法
			Object data = method.invoke(service, objects);
			//result.setData(data == null ? null : gson.toJson(data));
			result.setData(data == null ? null : JSON.toJSONString(data));
			// 跳出for循环
			break;
		}
		// 检查服务中是否有对应的方法
		if (!exist) {
			logger.info("请求无对应服务");
			return ERROR_CODE_NO_SERVICE;
		}
		//String resultJson = gson.toJson(result);
		String resultJson = JSON.toJSONString(result);
		return resultJson;
	}

}
