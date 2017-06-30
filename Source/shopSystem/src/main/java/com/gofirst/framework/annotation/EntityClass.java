package com.gofirst.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解在repository上使用，用于声明是哪个class
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityClass {

	/**
	 * 
	 * @return		class的值
	 */
	public Class<?> entityClass();
	
}
