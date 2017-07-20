package com.gofirst.framework.aspect;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
//import com.google.gson.Gson;

/**
 * 日志记录与消耗时间记录
 * 
 */
@Aspect
@Named
public class LogAspect {

	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(LogAspect.class);

	/**
	 * gson
	 */
	//@Inject
	//private Gson gson = null;

	/**
	 * controller切入点除了logController
	 */
	@Pointcut("execution(public * com..control..*.*(..)) && " + "!within(com..LogController)")
	private void controllerMethod() {
	}

	/**
	 * service所有公共方法的切入点
	 */
	@Pointcut("execution(public * com.gofirst.*.service..*.*(..))")
	private void serviceMethod() {
	}

	/**
	 * 打印进出service的日志
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("serviceMethod()")
	public Object serviceLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("进入服务 : " + className + " 的方法 : " + methodName);
		// 利用stopWatch把记时放到log中
		StopWatch stopWatch = new Log4JStopWatch();
		// 开始计时
		stopWatch.start();
		StringBuilder sb = new StringBuilder();
		Object[] arguments = joinPoint.getArgs();
		for (Object argument : arguments) {
			//sb.append(gson.toJson(argument));
			sb.append(JSON.toJSON(argument));
		}
		logger.info("调用服务的参数 : " + sb.toString());

		// 进入service的方法中
		Object obj = joinPoint.proceed();

		// 记时结束
		stopWatch.stop(className + " " + methodName);
		logger.info("离开服务 : " + className + " 的方法 : " + methodName);
		logger.info("从服务返回的结果 : " + obj);
		return obj;
	}

	/**
	 * 打印进出controller的日志
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("controllerMethod()")
	public Object controlLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		//请求输出
		logger.info("/************* joinpoint: " + joinPoint);
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		//进入接口
		logger.info("进入 controllerMethod : " + className + " 的方法 : " + methodName);
	
		Map<?, ?> paramMap = request.getParameterMap();
		
		// 打印请求中的参数到日志中
		StringBuilder sb = new StringBuilder();
		Set<?> keySet = paramMap.keySet();
		
		for (Iterator<?> iterator = keySet.iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			//测试
			logger.info("key : " +key.toString() +" : " +"paramMap.get(key): " + paramMap.get(key).toString());
			sb.append(JSON.toJSON(paramMap.get(key)));
		}
		logger.info("请求的参数 requestParameters : " + sb.toString());
		
		// 利用stopWatch把记时放到log中
		StopWatch stopWatch = new Log4JStopWatch();
		// 开始计时
		stopWatch.start();
		// 进入controller的方法中
		Object obj = joinPoint.proceed();
		// 记时结束
		stopWatch.stop(className + " " + methodName);
		
		logger.info("离开 controllerMethod : " + className + " 的方法 : " + methodName);
		logger.info("从controller返回的结果 : " + obj);
		return obj;
	}

}
