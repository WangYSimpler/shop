package com.gofirst.framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.annotation.EntityClass;
import com.gofirst.framework.annotation.Forbid;
import com.gofirst.framework.annotation.Gateway;
import com.gofirst.framework.annotation.SessionAttribute;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.session.FrameworkSession;
import com.gofirst.framework.session.impl.FrameworkSessionImpl;
//import com.google.gson.Gson;

public class Helper {

	public static String[] nosessions = null;

	/**
	 * json 串转化成对象数组
	 * 
	 * @param gson
	 * @param array
	 * @param method
	 * @param parameterCount
	 * @param pageable
	 * @param hasSessionAttributeAnnotation
	 * @return
	 * @throws JSONException
	 */
	public static Object[] json2ObjectArray( JSONArray array, Method method, int parameterCount,
			Pageable pageable, boolean hasSessionAttributeAnnotation) throws JSONException {

		Parameter[] parameters = method.getParameters();
		Object[] objects = null;
		if (parameterCount > 0 || pageable != null) {
			int number = parameterCount;
			if (pageable != null) {
				number = parameterCount + 1;
			}
			objects = new Object[number];
			if (pageable != null) {
				objects[parameterCount] = pageable;
			}

			if (parameters != null && parameters[0] != null && List.class.equals(parameters[0].getType())&& parameterCount != parameters.length) {
				parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
			}
			// json and java
			for (int i = 0; i < parameterCount; i++) {
				if (Constants.NULL.equals(array.get(i))) {
					objects[i] = null;
				} else if (int.class.equals(parameters[i].getType()) || Integer.class.equals(parameters[i].getType())) {
					objects[i] = (int)array.get(i);
				} else if (long.class.equals(parameters[i].getType()) || Long.class.equals(parameters[i].getType())) {
					objects[i] = array.getLong(i);
				} else if (double.class.equals(parameters[i].getType())
						|| Double.class.equals(parameters[i].getType())) {
					objects[i] = array.getDouble(i);
				} else if (String.class.equals(parameters[i].getType())) {
					objects[i] = array.getString(i);
				}else if (char.class.equals(parameters[i].getType())) {
					objects[i] = array.getString(i).charAt(0);
				}else {
					//JSON.parseObject(text, clazz)
					objects[i] = JSON.parseObject(array.get(i).toString(), parameters[i].getType());
				}
			}
		}
		// sessionAttribute
		if (hasSessionAttributeAnnotation) {
			Object[] newObjects = new Object[parameters.length];
			if (objects == null) {
				return newObjects;
			}
			for (int j = 0; j < newObjects.length; j++) {
				if (j < objects.length) {
					newObjects[j] = objects[j];
				}
			}
			return newObjects;
		}
		return objects;
	}

	/**
	 * 转换成数组
	 * 
	 * @param sorts
	 * @param page
	 * @param size
	 * @return
	 */
	public static Pageable convert2Pageable(String[] sorts, int pageIndex, int pageSize) {

		int page = pageIndex;
		int size = pageSize;
		/// 转换成Pageable类型
		Pageable pageable = null;

		if (sorts == null) {
			pageable = new PageRequest(page, size);
		} else {
			Sort sort = getAllSort(sorts);
			pageable = new PageRequest(page, size, sort);
		}
		
		return pageable;
	}
	

	public static Sort getAllSort(String[] orderSortStrs) {
		
		///排序字符串
		String[] orderSort = orderSortStrs;

		///中间获取
		List<Order> allOrder = new ArrayList<Order>();
		for(String orderStr : orderSort){
			 String SortStr = "";
			//排序方式
			if (orderStr.toLowerCase().contains(Constants.ASC)|| orderStr.toLowerCase().contains(Constants.DESC)) {
				
				if (orderStr.toLowerCase().contains(Constants.ASC)) {
					 SortStr = "ASC";
				}else if(orderStr.toLowerCase().contains(Constants.DESC)){
					SortStr = "DESC";
				}
				
			}else{
				
				 SortStr = "ASC";
			}
			
			List<String> ascList = getOrderSortList(orderStr);
			for(String ascPropertry :ascList)
			{
				allOrder.add(new Order(Direction.valueOf(SortStr), ascPropertry));
			}
		}
		Sort allSorts = new Sort(allOrder);
		
		return allSorts;
		
	}
	
	/**
	 * 获取排序的字符串 List
	 * @param orderSortStr
	 * @return
	 */
	public static  List<String> getOrderSortList(String orderSortStr )
	{
		String orderSort = orderSortStr;
		
		List<String>  propertiesList = null;
		///穿过来的字符串串不为空
		if ( !CommonUtil.isEmptyStr(orderSort)) {
			
			String[] splits = orderSort.split(Constants.COMMA);
			
			///拥有排序的关键字
			if (orderSort.toLowerCase().contains(Constants.ASC)|| orderSort.toLowerCase().contains(Constants.DESC)) {
				
				String[] propertiesOrder = new  String[splits.length-1];
				System.arraycopy(splits, 0, propertiesOrder, 0, splits.length-1);
				propertiesList =  java.util.Arrays.asList(propertiesOrder);
			}
			//
			else {
				String[] propertiesOrder = new  String[splits.length] ;
				System.arraycopy(splits, 0, propertiesOrder, 0, splits.length);
				propertiesList =  java.util.Arrays.asList(propertiesOrder);
			}
			
		}
		return propertiesList;
	}
	
	
	

	/**
	 * 获取所有的接口方法
	 * @param o 对象
	 * @return
	 */
	public static List<Method> getAllInterfaceMethods(Object o) {
		Class<?>[] interfaces = o.getClass().getInterfaces();
		List<Method> methodList = new ArrayList<Method>();
		for (Class<?> inter : interfaces) {
			Method[] methods = inter.getMethods();
			for (Method method : methods) {
				methodList.add(method);
			}
		}
		return methodList;
	}

	public static Object castId(Object id, Class<?> idClass) {
		if (Long.class.equals(idClass)) {
			return Long.parseLong(id.toString());
		} else if (long.class.equals(idClass)) {
			return (long) id;
		} else if (Integer.class.equals(idClass)) {
			return Integer.parseInt(id.toString());
		} else if (int.class.equals(idClass)) {
			return (int) id;
		} else if (String.class.equals(idClass)) {
			return id.toString();
		}else if (BigDecimal.class.equals(idClass)) {
			return  new BigDecimal(id.toString());
		}
		return null;
	}

	public static Class<?> findEntityClass(Class<?> repositoryClass, String repositoryName) {
		
		Class<?>[] classes = repositoryClass.getInterfaces();
		Class<?> repositoryInterfaceClass = null;
		for (Class<?> cl : classes) {
			if (cl.getTypeName().toLowerCase().contains(repositoryName.toLowerCase())) {
				repositoryInterfaceClass = cl;
				break;
			}
		}
		Class<?> entityClass = null;
		for (Annotation annotation : repositoryInterfaceClass.getAnnotations()) {
			if (annotation instanceof EntityClass) {
				entityClass = ((EntityClass) annotation).entityClass();
			}
		}
		return entityClass;
	}

	public static Class<?> getIDType(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		Class<?> IDClass = null;
		for (Field field : fields) {
			for (Annotation a : field.getAnnotations()) {
				if (a instanceof Id) {
					IDClass = field.getType();
					break;
				}
			}
		}
		return IDClass;
	}

	/**
	 * 获取主键名称
	 * 
	 * @param entityClass
	 * @return
	 */
	public static String getPKName(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		String PKName = null;
		for (Field field : fields) {
			for (Annotation a : field.getAnnotations()) {
				if (a instanceof Id) {
					PKName = field.getName();
					break;
				}
			}
		}
		return PKName;
	}

	public static String removeBracket(List<String> regexResult) {
		StringBuilder sb = new StringBuilder();
		for (String s : regexResult) {
			int beginIdx = s.indexOf(Constants.B_BRACKET);
			int endIdx = s.indexOf(Constants.E_BRACKET);
			sb.append(s.subSequence(beginIdx + 1, endIdx));
		}
		return sb.toString();
	}

	public static Class<?>[] cast2Class(Object[] objs) {
		if (objs != null) {
			Class<?>[] newObjs = new Class[objs.length];
			for (int i = 0; i < objs.length; i++) {
				newObjs[i] = objs[i].getClass();
			}
			return newObjs;
		}
		return null;
	}

	public static boolean hasSessionAttributeAnnotation(Method method) {
		Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getAnnotationsByType(SessionAttribute.class).length > 0) {
				return true;
			}
		}
		return false;
	}

	public static int getSessionAttributeAnnotationCount(Method method) {
		int count = 0;
		Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getAnnotation(SessionAttribute.class) != null) {
				count++;
			}
		}
		return count;
	}

	public static void setValueForProp(Object object) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			SessionAttribute sessionAttribute = field.getAnnotation(SessionAttribute.class);
			if (sessionAttribute == null) {
				continue;
			}
			String key = sessionAttribute.value();
			FrameworkSession session = FrameworkSessionImpl.getSession();
			Object value = session.getAttribute(key);
			field.setAccessible(true);
			field.set(object, value);
		}
	}

	public static void setValueForParam(Method method, Object[] parameters) {

		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		int index = 0;
		for (Annotation[] annotations : paramAnnotations) {
			for (Annotation an : annotations) {
				if (an instanceof SessionAttribute) {
					SessionAttribute sessionAttribute = (SessionAttribute) an;
					String key = sessionAttribute.value();
					FrameworkSession session = FrameworkSessionImpl.getSession();
					parameters[index] = session.getAttribute(key);
				}
			}
			index++;
		}
	}

	public static boolean hasGatewayAnnotation(Object object) {
		Class<?>[] allInterfaceClass = object.getClass().getInterfaces();
		for (Class<?> c : allInterfaceClass) {
			if (c.getDeclaredAnnotation(Gateway.class) != null) {
				return true;
			}
		}

		return false;

	}

	/**
	 * jointPoint
	 * 
	 * @param joinPoint
	 * @param frameworkRealm
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasPermission(Object object, Method method, AuthorizingRealm frameworkRealm, String type,
			Logger logger) throws PermissionException {
		String methodName = method.getName();
		
		if (isNosession(object, methodName)) {
			return true;
		}

		String sessionId = FrameworkSession.getSessionId();
		Subject subject = new Subject.Builder().sessionId(sessionId).buildSubject();
		List<String> classNames = new ArrayList<String>();

		Class<?>[] interfacesClasses = object.getClass().getInterfaces();
		for (Class<?> c : interfacesClasses) {
			classNames.add(c.getSimpleName());
		}

		checkForbiddenMethod(interfacesClasses, methodName, logger);

		if (!subject.isAuthenticated()) {
			logger.info("没有权限！");
			return false;
		}
		
		Session session = subject.getSession();
		Set<String> permissions = new HashSet<String>();
		if (session.getAttribute(Constants.SESSION_PERMISSION) == null) {
			subject.hasRole("");
			Cache<Object, AuthorizationInfo> cache = frameworkRealm.getAuthorizationCache();
			for (AuthorizationInfo authorization : cache.values()) {
				permissions.addAll(authorization.getStringPermissions());
			}
			// ��cache�ŵ�session��
			session.setAttribute(Constants.SESSION_PERMISSION, permissions);
			logger.info("session: " + cache.size());
		} else {
			permissions = (Set<String>) session.getAttribute(Constants.SESSION_PERMISSION);
			logger.info("session 大小: " + permissions.size());
		}
		// �õ�ָ�����͵�Ȩ��
		for (String permission : permissions) {
			if (permission.contains(type)) {
				String part = permission.split(Constants.COLON)[1];
				String[] nameArray = part.split(Constants.PUNCTUATION);
				boolean contains = false;
				for (String className : classNames) {
					if (className.equalsIgnoreCase(nameArray[0])) {
						contains = true;
						break;
					}
				}
				if (contains) {
					//Myrepository.*
					if (nameArray[1].contains(Constants.STAR)) {
						return true;
					}
					if (methodName.equals(nameArray[1]) || (methodName != null && methodName.matches(methodName))) {
						return true;
					}
				}

			}
		}
		logger.info("type: "+ type + " " + classNames + " " + methodName);
		return false;
	}

	/**
	 * 是否是 nosession
	 * 
	 * @param object
	 * @param methodName
	 * @return
	 */
	public static boolean isNosession(Object object, String methodName) {
		List<String> classNames = new ArrayList<String>();
		if (object != null) {
			
			Class<?>[] interfacesClasses = object.getClass().getInterfaces();
			for (Class<?> c : interfacesClasses) {
				classNames.add(c.getSimpleName());
			}
		}

		if (nosessions != null) {
			for (String permission : nosessions) {
				String part = permission.split(Constants.COLON)[1];
				String[] nameArray = part.split(Constants.PUNCTUATION);
				boolean contains = false;
				for (String className : classNames) {
					if (className.equalsIgnoreCase(nameArray[0])) {
						contains = true;
						break;
					}
				}
				if (contains) {
					if (nameArray[1].contains(Constants.STAR)) {
						return true;
					}
					if (methodName.equals(nameArray[1])) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * 是否有 Pageable参数
	 * 
	 * @param method
	 * @return
	 */
	public static boolean hasPageableParameter(Method method) {
		Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			System.out.println("参数该方法的类型" + parameter.getParameterizedType());
			if (Pageable.class.equals(parameter.getParameterizedType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查禁止的方法， 使用禁止注解 :@Forbid
	 * 
	 * @param joinPoint
	 * @throws PermissionException
	 */
	private static void checkForbiddenMethod(Class<?>[] interfacesClasses, String methodName, Logger logger)
			throws PermissionException {

		// 禁止方法注解打在方法上
		for (Class<?> c : interfacesClasses) {
			Annotation annotation = c.getDeclaredAnnotation(Forbid.class);
			if (annotation == null) {
				return;
			}
			Forbid forbid = (Forbid) annotation;
			String[] forbiddenMethods = forbid.forbiddenMethods();
			for (String name : forbiddenMethods) {
				if (name.equals(methodName)) {
					logger.info("forbidden method annotation declared : " + c.getSimpleName() + " " + methodName);
					throw new PermissionException("forbidden method annotation declared");
				}
			}
		}
	}

}
