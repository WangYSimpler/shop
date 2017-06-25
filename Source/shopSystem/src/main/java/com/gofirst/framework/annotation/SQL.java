package com.gofirst.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 此注解用在自定义sql查询上面
 *
 */
@Repeatable(MultiSQL.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQL {

	/**
	 *枚举数据库类型 
	 *
	 */
	public enum DBEnum{
		MYSQL, ORACLE;
	}
	
	/**
	 * 
	 * @return	数据库
	 */
	public DBEnum database();
	
	
	/**
	 *是否为动态sql，默认为否 
	 */
	public boolean isDynamic() default false;
	
	/**
	 * 
	 * @return	自定义sql的内容
	 */
	public String sql();
}
