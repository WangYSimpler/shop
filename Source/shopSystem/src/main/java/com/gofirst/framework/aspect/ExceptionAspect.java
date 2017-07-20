package com.gofirst.framework.aspect;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_DB;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_ACCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM_JSON;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM_VALID;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_UNKNOWN;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Set;

//import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.core.annotation.Order;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.response.ResponseResult;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.exception.ServiceException;
//import com.google.gson.Gson;

/**
 * controller传入值和返回值 utf-8编码，同时记录入参和出参
 * 
 */
@Aspect
@Named
public class ExceptionAspect {

	//logger
	private static final Logger logger = Logger.getLogger(ExceptionAspect.class);

	@Pointcut("execution(public * com..control.*.*(..)) ")
	private void anyMethod() {
	}

	/**
	 * 给入参使用utf-8解码
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("anyMethod()")
	@Order(0)
	public Object exceptionHandle(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpServletResponse response = null;
		Object[] args = joinPoint.getArgs();
		for (Object obj : args) {
			if (obj instanceof HttpServletResponse) {
				response = (HttpServletResponse) obj;
				break;
			}
		}

		try {
			Object obj = joinPoint.proceed(joinPoint.getArgs());
			return obj;
		} catch (BeansException e) {
			logger.info("请求无对应服务 " + e.getMessage(), e);
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		} catch (JSONException e1) {
			logger.info("请求参数无法转换成json " + e1.getMessage(), e1);
			response.setHeader(ERROR_HEADER, ERROR_CODE_PARAM_JSON);
			return null;
		} catch (NoSuchMethodException e) {
			logger.info("请求无对应repository服务 " + e.getMessage(), e);
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		} catch (SQLException e1) {
			logger.info("数据库无法获得连接 " + e1.getMessage(), e1);
			response.setHeader(ERROR_HEADER, ERROR_CODE_DB);
			return null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if (e.getCause() instanceof PermissionException
					|| (e.getCause() != null && e.getCause().getCause() instanceof PermissionException)) {
				logger.info("无权限访问 " + e.getMessage(), e);
				response.setHeader(ERROR_HEADER, ERROR_CODE_NO_ACCESS);
				return null;
			}
			// 调用远程服务，校验错误
			if (e.getCause() instanceof RpcException) {
				if (e.getCause().getCause() instanceof ConstraintViolationException) {
					ConstraintViolationException ve = (ConstraintViolationException) e.getCause().getCause();
					Set<ConstraintViolation<?>> violations = ve.getConstraintViolations();
					logger.info("validation failed : " + violations.toString());
				}
				logger.info("RPC exception ", e);
				response.setHeader(ERROR_HEADER, ERROR_CODE_PARAM_VALID);
				return null;
			}
			logger.info("请求参数类型错误 " + e.getMessage(), e);
			response.setHeader(ERROR_HEADER, ERROR_CODE_PARAM);
			return null;
		} catch (Exception e) {
			if (e instanceof ServiceException) {
				logger.info("后台service发生错误 " + e.getMessage(), e);
				ResponseResult result = new ResponseResult();
				result.setErrorCode(((ServiceException) e).getErrorCode());
				return JSON.toJSON(result);
			} else if (e instanceof PermissionException) {
				logger.info("无权限访问 " + e.getMessage(), e);
				response.setHeader(ERROR_HEADER, ERROR_CODE_NO_ACCESS);
				return null;
			} else {
				logger.info("后台业务处理遇到未知错误 " + e.getMessage(), e);
				response.setHeader(ERROR_HEADER, ERROR_CODE_UNKNOWN);
				return null;
			}
		}
	}

}