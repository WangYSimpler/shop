package com.gofirst.framework.systemService.impl;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.bean.PagingResponseResult;
import com.gofirst.framework.bean.ResponseResult;
import com.gofirst.framework.configure.SystemConfigProperties;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.exception.ServiceException;
import com.gofirst.framework.systemService.RepositoryQueryService;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;
//import com.google.gson.Gson;

/**
 *
 * 查询的repository服务
 *
 */
@Named
public class RepositoryQueryServiceImpl implements RepositoryQueryService {

	/**
	 * logger 日志
	 */
	private static final Logger logger = Logger.getLogger(RepositoryQueryServiceImpl.class);

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

	//@Inject
	//private JSON json = null;
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
	@Override
	@Transactional(readOnly = true)
	public String query(String repositoryName, String funcName, Object props, Pageable pageable)
			throws BeansException, JSONException, IllegalAccessException, IllegalArgumentException,InvocationTargetException, PermissionException, ServiceException {
		String resultJson = null;
		// 得到对应repository名字的服务
		Object repository = context.getBean(repositoryName);
		// 获取repository 下面存在的方法
		List<Method> methods = Helper.getAllInterfaceMethods(repository);
		// 查找名称为findByPropName的方法，且方法参数个数和前台传过来参数个数相同
		boolean exist = false;
		for (Method method : methods) {
			
			///判断请求 DAO 里面是有有该方法
			if (!funcName.equals(method.getName())) {
				continue;
			}
			
			boolean hasPageableParameter = Helper.hasPageableParameter(method);
			// 查看找到的方法和前台传过来的参数是否都有,或者都没有pageable参数
			if ((hasPageableParameter && pageable == null) || (!hasPageableParameter && pageable != null)) {
				continue;
			}
			// 判断此方法和类是否为外部可以访问
			if (!Helper.hasGatewayAnnotation(repository)) {
				logger.info("there is no gateway annotation : " + repositoryName);
				throw new PermissionException();
			}
			// 外部可访问的则做权限判断
			/**
			 * if (!Helper.hasPermission(repository, method, frameworkRealm,
			 * Constants.DAO, logger)) { throw new
			 * PermissionException("没有访问方法的权限"); }
			 */
			
			Object[] objects = null;
			JSONArray array = null;
			int paramCountFromReq = 0;
			int num = 0;
			if (props == null) {
				// 判断方法含有@sessionAttribute的参数个数
				int sessionAttributeAnnotationCount = Helper.getSessionAttributeAnnotationCount(method);
				num += sessionAttributeAnnotationCount;
				// 方法参数个数和前台传过来参数个数相同
				if (num != method.getParameterCount()) {
					continue;
				}
				// 根据方法的参数类型，将json转换成java对象
				objects = Helper.json2ObjectArray( array, method, paramCountFromReq, pageable,sessionAttributeAnnotationCount > 0 ? true : false);
			}
			
			
			if (props instanceof String ) {// 传入参数是数组，前台按照参数位置来设置
				array = new JSONArray();
				if (!props.equals("[]")) {
					
					array = 	(JSONArray)JSON.parse(props.toString()); 
					//array.fluentAdd(props);
				}
				
				num = paramCountFromReq = array.size();
				if (pageable != null) {
					num++;
				}
				// 判断方法含有@sessionAttribute的参数个数
				int sessionAttributeAnnotationCount = Helper.getSessionAttributeAnnotationCount(method);
				num += sessionAttributeAnnotationCount;
				// 方法参数个数和前台传过来参数个数相同
				if (num != method.getParameterCount()) {
					continue;
				}
				// 根据方法的参数类型，将json转换成java对象
				objects = Helper.json2ObjectArray(array, method, paramCountFromReq, pageable,sessionAttributeAnnotationCount > 0 ? true : false);
			} else if (props instanceof Map) {// 传入参数为根据参数名的key,value形式，前台按照参数名字来设值
				@SuppressWarnings("rawtypes")
				Map propMap = (Map) props;
				Parameter[] parameters = method.getParameters();
				objects = new Object[parameters.length];
				int index = -1;
				for (Parameter p : parameters) {
					index++;
					// 处理分页pageable的参数
					if (Pageable.class.equals(p.getParameterizedType().getClass())) {
						objects[index] = pageable;
						continue;
					}
					String paramName = p.getName();
					if (propMap.get(paramName) == null) {
						objects[index] = null;
						continue;
					}
					// 因为request.getParameterMap()返回的时数组，而我们只允许一一对应key-value
					Object[] values = (Object[]) propMap.get(paramName);
					Object value = values[0];
					objects[index] = JSON.parseObject((String) value, p.getType());
					
					//objects[index] = gson.fromJson((String) value, p.getType());

				}
			} else {
				throw new ServiceException(1, "request parameter type is wrong");
			}

			exist = true;

			Object data = method.invoke(repository, objects);
			if (pageable == null) {
				ResponseResult result = new ResponseResult();
				//result.setData(gson.toJson(data));
				result.setData(JSON.toJSONString(data));
				
				//resultJson = gson.toJson(result);
				resultJson = JSON.toJSONString(result);
			} else {
				PagingResponseResult pagingResult = new PagingResponseResult();
				// 把当前页的数据，总共条数，总共页数封装在PagingResponseResult中返回
				Page<?> pageData = (Page<?>) data;
				pagingResult.setData(JSON.toJSONString(pageData.getContent()));
				pagingResult.setTotalCount(pageData.getTotalElements());
				pagingResult.setPageCount(pageData.getTotalPages());
				
				resultJson = JSON.toJSONString(pagingResult);
				
				// wangyong
				//resultJson = gson.toJson(pagingResult);
			}
			// 跳出for循环
			break;
		}
		// 检查服务中是否有对应的方法
		if (!exist) {
			logger.info("请求无对应repository服务 " + funcName);
			return ERROR_CODE_NO_SERVICE;
		}
		//返回结果
		return resultJson;
	}

}
