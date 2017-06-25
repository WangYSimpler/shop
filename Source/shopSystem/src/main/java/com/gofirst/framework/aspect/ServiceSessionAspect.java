package com.gofirst.framework.aspect;

import java.lang.reflect.Field;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.gofirst.framework.session.FrameworkSession;
import com.gofirst.framework.session.impl.FrameworkSessionImpl;
import com.gofirst.framework.util.Constants;

@Aspect
@Named
public class ServiceSessionAspect {

	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(ServiceSessionAspect.class);
	
	@Pointcut("execution(public * com..service..*.*(..)) ")
	private void anyMethod() {
	}
	
	@Around("anyMethod()")
	public Object setSessionForService(ProceedingJoinPoint joinPoint) throws Throwable {
		// 从threadLocal中拿到sessionId
		Object target = joinPoint.getTarget();
		Class<?> targetClass = target.getClass();
		//找到类型为Session的属性
		try {
			Field field = targetClass.getDeclaredField(Constants.SESSION);
			//给服务的session属性赋值
			field.setAccessible(true);
			if(field.get(target) == null) {
				FrameworkSession session = FrameworkSessionImpl.getSession();
				field.set(target, session);
			}
		} catch(Exception e) {
			logger.info("service " + targetClass.getName() + "do not have field session");
		}
		
		return joinPoint.proceed();
	}

	
}
