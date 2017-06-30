package com.gofirst.framework.util;

import java.util.Collection;
import java.util.Map;



import org.apache.commons.lang3.StringUtils;

import com.gofirst.framework.exception.ServiceException;


/**
 * 断言工具类
 */
public abstract class AssertUtil {
	/**
	 * 默认对象不为<code>null</code>错误信息
	 */
	private final static String DEFAULT_NOT_NULL_MESSAGE = "该对象不能为空!";
	/**
	 * 默认Map不能为空错误信息
	 */
	private final static String DEFAULT_NOT_EMPTY_MAP_MESSAGE = "该Map不能为空!";
	/**
	 * 默认集合不能为空错误信息
	 */
	private final static String DEFAULT_NOT_EMPTY_COL_MESSAGE = "该集合不能为空!";
	/**
	 * 默认字符串不能为空错误信息
	 */
	private final static String DEFAULT_NOT_EMPTY_STR_MESSAGE = "该字符串不能为空!";
	
	/**
	 * 断言对象不为空, 否则抛出一个以参数message构造的{@link BaseException}对象
	 * @param obj Object 所需判断的对象
	 * @param message String 错误信息, 用于构造异常对象
	 * @throws ServiceException 
	 * @see BaseException
	 */
	public static void notNull(Object obj, String message) throws ServiceException {
		if ( null == obj ) 
			throw new ServiceException(1, message);
	}
	/**
	 * 使用{@link DEFAULT_NOT_NULL_MESSAGE 默认错误信息}进行断言
	 * @param obj Object 所需判断对象
	 * @throws ServiceException 
	 * @see #notNull(Object, String)
	 */
	public static void notNull(Object obj) throws ServiceException {
		notNull(obj, DEFAULT_NOT_NULL_MESSAGE);
	}
	/**
	 * 判断一个{@link Map}对象是否为空,为空时抛出一个以参数message构造的{@link BaseException}对象
	 * @param map Map 所需判断的map对象
	 * @param message String 异常信息, 用于构造异常
	 * @throws ServiceException 
	 * @see BaseException
	 */
	public static void notEmpty(Map < ?, ? > map, String message) throws ServiceException {
		if ( null == map || map.isEmpty() ) 
			throw new ServiceException(2, message);
	}
	/**
	 * 使用{@link #DEFAULT_NOT_EMPTY_MAP_MESSAGE 默认异常信息}对map对象进行不为空断言
	 * @param map Map 所需断言的map对象
	 * @throws ServiceException 
	 * @see #notEmpty(Map,String)
	 */
	public static void notEmpty(Map< ?, ? > map) throws ServiceException {
		notEmpty(map, DEFAULT_NOT_EMPTY_MAP_MESSAGE);
	}
	/**
	 * 对一个{@link Collection 集合}对象进行不为空断言, 失败时抛出一个以message构建的{@link BaseException}对象
	 * @param collection Collection 集合对象
	 * @param message String 异常信息,用于构造异常对象
	 * @throws ServiceException 
	 * @see BaseException
	 */
	public static void notEmpty( Collection<?> collection, String message ) throws ServiceException {
		if ( null == collection || collection.isEmpty() )
			throw new ServiceException(2, message);
	}
	/**
	 * 使用{@link #DEFAULT_NOT_EMPTY_COL_MESSAGE 默认异常信息}对集合对象进行不为空断言.
	 * @param collection Collection 集合对象
	 * @throws ServiceException 
	 * @see #notEmpty(Collection, String)
	 */
	public static void notEmpty( Collection<?> collection ) throws ServiceException {
		notEmpty(collection, DEFAULT_NOT_EMPTY_COL_MESSAGE);
	}
	
	/**
	 * 对一个字符串进行不为空断言,失败时将抛出一个以参数message构建的{@link BaseException}对象
	 * @param arg String 所需断言的字符串对象
	 * @param message String 异常信息,用于构建异常
	 * @throws ServiceException 
	 * @see BaseException
	 */
	public static void notEmpty(String arg, String message) throws ServiceException {
		if ( StringUtils.isEmpty(arg) )
			throw new ServiceException(2, message);
	}
	/**
	 * 使用{@link #DEFAULT_NOT_EMPTY_STR_MESSAGE 默认异常信息}对字符串进行不为空断言
	 * @param arg String 所需断言字符串
	 * @throws ServiceException 
	 * @see #notEmpty(String, String)
	 */
	public static void notEmpty(String arg) throws ServiceException {
		notEmpty(arg, DEFAULT_NOT_EMPTY_STR_MESSAGE);
	}

}
