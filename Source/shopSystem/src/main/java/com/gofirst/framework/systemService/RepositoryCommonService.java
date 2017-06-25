package com.gofirst.framework.systemService;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeansException;

import com.gofirst.framework.exception.PermissionException;

/**
 * 增、删、更新 repository服务
 *
 */
public interface RepositoryCommonService {

	/**
	 * 增加，删除，更新 的repository的通用方法
	 *  
	 * @param type 类型，增加，删除或者更新
	 * @param params request的参数类型
	 * @param body request body
	 * @param repositoryName  访问的后台repository名字
	 * @param id  
	 * 
	 * 返回的错误代码
	 * @return 错误码
	 * 
	 * 可能出现抛出的异常
	 * @throws BeansException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public Object commonOperator(String type, String params, String body, String repositoryName, Object id)
			throws BeansException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, PermissionException, NoSuchFieldException, SecurityException;

}
