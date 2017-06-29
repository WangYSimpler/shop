package com.gofirst.framework.systemService;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeansException;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.exception.ServiceException;

/**
 *
 * 查询的repository服务
 *
 */
public interface RepositoryQueryService {

	/**
	 * 执行repository的各种查询方法
	 * 
	 * @param repositoryName  访问的后台数据模型名字
	 * @param funcName 方法名
	 * @param props 根据查找的属性值
	 * @param pageable  分页 
	 * 
	 * 返回数据的格式
	 * @return response json
	 * 
	 * 查询数据抛出异常
	 * @throws BeansException
	 * @throws JSONException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws ServiceException
	 */
	public String query(String repositoryName, String funcName, Object props, Pageable pageable)
			throws BeansException, JSONException, IllegalAccessException, IllegalArgumentException,InvocationTargetException, PermissionException, ServiceException;
}
