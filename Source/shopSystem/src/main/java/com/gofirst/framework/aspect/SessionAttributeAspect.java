package com.gofirst.framework.aspect;

import java.lang.reflect.Method;

import javax.inject.Named;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.gofirst.framework.annotation.SessionAttribute;
import com.gofirst.framework.session.impl.FrameworkSessionImpl;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;

/**
 *
 * 切面用来解析sessionAttribute注解的值
 *
 */
@Aspect
@Named
public class SessionAttributeAspect {

	@Pointcut("execution(public * com..service..*.*(..)) ")
	private void anyServiceMethod() {
	}

	@Pointcut("execution(public * org.springframework.data.repository.Repository+.*(..))")
	private void anyRepository() {
	}

	@Around("anyServiceMethod() || anyRepository() ")
	public Object setSessionAttribute(ProceedingJoinPoint joinPoint)
			throws Throwable {

		Object[] parameters = joinPoint.getArgs();
		// 反射拿到method
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		// 处理save, update, partUpdate时，属性有@SessionAttribute
		if (Constants.SAVE.equals(method.getName())
				|| Constants.UPDATE.equals(method.getName())
				|| Constants.PARTUPDATE.equals(method.getName())) {
			// 保存跟新。因为参数只有一个就是需要保存和更新的对象
			Helper.setValueForProp(parameters[0]);
			return joinPoint.proceed();
		}

		// 根据方法参数的注解@sessionAttribute，从session中去出key的值设给参数
		Helper.setValueForParam(method, parameters);

		// 处理需要将返回值放入session
		Object obj = joinPoint.proceed(parameters);
		SessionAttribute[] sessionAttributes = method.getAnnotationsByType(SessionAttribute.class);

		if (sessionAttributes == null || sessionAttributes.length == 0) {
			return obj;
		}

		FrameworkSessionImpl.getSession().setAttribute(
				sessionAttributes[0].value(), obj);

		return obj;

	}

}
