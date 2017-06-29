package com.gofirst.framework.systemService.impl;

import static com.gofirst.framework.util.Constants.DELETE;
import static com.gofirst.framework.util.Constants.PARTUPDATE;
import static com.gofirst.framework.util.Constants.SAVE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_ID;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM_JSON;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_PARAM_VALID;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.gofirst.framework.configure.SystemConfigProperties;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.systemService.RepositoryCommonService;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;
//import com.google.gson.Gson;

/**
 * 增，删，改的repository服务
 *
 */
@Named
public class RepositoryCommonServiceImpl implements RepositoryCommonService {

	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(RepositoryCommonServiceImpl.class);

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
	 * java validator
	 */
	@Inject
	private Validator validator;

	/**
	 * shiro realm
	 */
	@SuppressWarnings("unused")
	private AuthorizingRealm frameworkRealm = null;

	@PostConstruct
	public void init() {
		// 根据配置文件拿到对应的realm
		frameworkRealm = (AuthorizingRealm) context.getBean(sysConfig.getProperty(Constants.REALM_BEAN_NAME));
	}

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
	@Override
	@Transactional(readOnly = false)
	public Object commonOperator(String type, String params, String body, String repositoryName, Object id)
			throws BeansException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,InvocationTargetException, PermissionException, NoSuchFieldException, SecurityException {
		// 得到对应repository名字的服务
		Object repository = context.getBean(repositoryName);
		Class<?> repositoryClass = repository.getClass();
		// 找到entityClass
		Class<?> entityClass = Helper.findEntityClass(repositoryClass, repositoryName);
		
		//entity class wangyong 20170605
		if (entityClass == null) {
			logger.info("/*** 请求参数为NULL, 无法转换  java Object ***/");
			return ERROR_CODE_PARAM_JSON;
		}
		//删除类型
		if (type.equals(DELETE)) {
			// 反射找到Id的类型
			Class<?> idClass = Helper.getIDType(entityClass);
			if (idClass == null) {
				logger.info("entity定义错误，没有主键Id");
				return ERROR_CODE_ID;
			}
			// 反射找到CrudRepository的delete(ID id)方法
			Method methodDelete = repositoryClass.getDeclaredMethod(DELETE, Serializable.class);
			// 判断此方法和类是否为外部可以访问
			if (!Helper.hasGatewayAnnotation(repository)) {
				logger.info("there is no gateway annotation : " + repositoryName);
				throw new PermissionException();
			}
			
			// wangyong 20170605
			// 外部可访问的则做权限判断  
			/**
			 * if (!Helper.hasPermission(repository, methodDelete,
			 * frameworkRealm, Constants.DAO, logger)) { throw new
			 * PermissionException("没有访问方法的权限"); }
			 */
			
			methodDelete.invoke(repository, Helper.castId(id, idClass));
		} else {
			// 反射找到CrudRepository的save(S Entity)方法,如果是局部跟新调用局部跟新方法
			Method method = null;
			if (type.equals(PARTUPDATE)) {
				method = repositoryClass.getDeclaredMethod(PARTUPDATE,new Class[] { Serializable.class, Object.class });
			} else {
				method = repositoryClass.getDeclaredMethod(SAVE, Object.class);
				//method = repositoryClass.getDeclaredMethod(SAVE,new Class[] { Serializable.class, Object.class });
			}
			// 判断此方法和类是否为外部可以访问
			if (!Helper.hasGatewayAnnotation(repository)) {
				logger.info("there is no gateway annotation : " + repositoryName);
				throw new PermissionException();
			}
			// 外部可访问的则做权限判断
			/*
			 * if (!Helper.hasPermission(repository, method, frameworkRealm,
			 * Constants.DAO, logger)) { throw new
			 * PermissionException("没有访问方法的权限"); }
			 */
			// 跟新post请求，数据为参数，保存数据直接在request body中
			if (!type.equals(SAVE)) {
				body = params;
			}
			
			//Object obj = JSON.parseObject(body, entityClass);
			//System.out.println("body:" + body);
			//body = "{\"createDatetime\":\"2017-12-05 12:12:16\",\"createUser\":\"admin1\",\"deleteFlag\":\"0\",\"employeeId\":\"1\",\"orgDeptId\":\"1\",\"organizationId\":\"1\",\"updateUser\":\"王勇\",\"userId\":\"22\",\"userName\":\"王勇\",\"userNo\":\"demo\",\"userPd\":\"c4ca4238a0b923820dcc509a6f75849b\",\"userStatus\":\"1\",\"userType\":\"3\"}";
			Object obj = JSON.parseObject(body, entityClass);
			if (type.equals(PARTUPDATE)) {
				// 反射找到Id的类型
				Class<?> idClass = Helper.getIDType(entityClass);
				if (idClass == null) {
					logger.info("entity定义错误，没有主键Id");
					return ERROR_CODE_ID;
				}
				method.invoke(repository, new Object[] { Helper.castId(id, idClass), obj });
			} else {
				Class<?>[] interfacesClass = repositoryClass.getInterfaces();
				boolean isRemoteService = false;
				// 是否dubbo远程调用
				for (Class<?> c : interfacesClass) {
					if (c.getName().contains("com.alibaba.dubbo")) {
						isRemoteService = true;
						break;
					}
				}
				if (!isRemoteService) {
					// 验证对象属性的正确性
					Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
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
				// 新增数据返回id到前台
				if (type.equals(SAVE)) {
					String PKname = Helper.getPKName(entityClass);
					Object savedObj = method.invoke(repository, obj);
					Class<?> savedObjClass = savedObj.getClass();
					Field field = savedObjClass.getDeclaredField(PKname);
					field.setAccessible(true);
					return field.get(savedObj);
				}
				method.invoke(repository, obj);
			}
		}
		// 返回成功信息
		return ERROR_CODE_SUCCESS;
	}

}
