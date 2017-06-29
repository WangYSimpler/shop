package com.gofirst.framework.control;

import static com.gofirst.framework.util.Constants.DELETE;
import static com.gofirst.framework.util.Constants.FORMAT;
import static com.gofirst.framework.util.Constants.PARTUPDATE;
import static com.gofirst.framework.util.Constants.SAVE;
import static com.gofirst.framework.util.Constants.UPDATE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
//import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.bean.ResponseResult;
import com.gofirst.framework.exception.PermissionException;
import com.gofirst.framework.exception.ServiceException;
import com.gofirst.framework.systemService.RepositoryCommonService;
import com.gofirst.framework.systemService.RepositoryQueryService;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;
//import com.google.gson.Gson;

/**
 * 通用dao处理器 根据模型名字转发到相应的Repository.
 */
@Controller
public class RepositoryController {

	/**
	 * loggerbvc
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RepositoryController.class);

	/**
	 * spring context
	 */
	@SuppressWarnings("unused")
	@Autowired(required = true)
	private ApplicationContext context;

	/**
	 * gson
	 */
	//@Autowired(required = true)
	//private Gson gson = null;

	@Autowired(required = true)
	private RepositoryCommonService repoCommonService;

	@Autowired(required = true)
	private RepositoryQueryService repoQueryService;

	/**
	 * 处理dao的save的请求.
	 * 
	 * @param request
	 *            http request
	 * @param response
	 *            http response
	 * @param repositoryName
	 *            访问的后台repository名字
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws BeansException
	 * @throws PermissionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@RequestMapping(value = "/repository/{repositoryName}", method = RequestMethod.PUT, produces = FORMAT)
	@ResponseBody
	public String doSave(HttpServletRequest request, HttpServletResponse response, @RequestBody String body,@PathVariable String repositoryName)
			throws BeansException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,	InvocationTargetException, PermissionException, NoSuchFieldException, SecurityException {
		//返回结果
		Object obj = repoCommonService.commonOperator(SAVE, null, body, repositoryName, -1);
		
		
		if (obj instanceof String || obj instanceof BigDecimal  ) {
			ResponseResult result = new ResponseResult();
			// 设置成功的返回头
			response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
			result.setData(JSON.toJSONString(obj));
			
			String successReslutStr = "";
			
			if (obj instanceof String ) {
				successReslutStr = JSON.toJSONString(result);
			}else if (obj instanceof BigDecimal) {
				successReslutStr = JSON.toJSONString(obj);
			}
			
			return successReslutStr;
		}
		
		// 根据服务返回值，设置返回头error_code信息
		String errorCode = obj.toString();
		
		/**
		 * String errorCode ="";
		 * if (obj instanceof BigDecimal) {
		 *	errorCode = obj.toString();
		 * }else{
		 * errorCode = (String) obj;
		 *  }
		*/
		
		response.setHeader(ERROR_HEADER, errorCode);
		return null;
	}

	/**
	 * 处理dao的更新的请求.
	 * @param request http-request
	 * @param response http-response
	 * @param repositoryName  后台repository名字
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws BeansException
	 * @throws PermissionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@RequestMapping(value = "/repository/{repositoryName}/{id}", method = RequestMethod.POST, produces = FORMAT)
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, @PathVariable String repositoryName,
			@PathVariable Object id)
			throws BeansException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, PermissionException, NoSuchFieldException, SecurityException {
		String params = request.getParameter(Constants.PARAMS);
		String errorCode = (String) repoCommonService.commonOperator(UPDATE, params, null, repositoryName, id);
		// 根据服务返回值，设置返回头error_code信息
		response.setHeader(ERROR_HEADER, errorCode);
	}

	/**
	 * 部分跟新
	 * 
	 * @param request
	 * @param response
	 * @param repositoryName
	 * @param id
	 * @throws BeansException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@RequestMapping(value = "/repository/partUpdate/{repositoryName}/{id}", method = RequestMethod.POST, produces = FORMAT)
	public void doPartUpdate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String repositoryName, @PathVariable Object id)
			throws BeansException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, PermissionException, NoSuchFieldException, SecurityException {
		String params = request.getParameter(Constants.PARAMS);
		String errorCode = (String) repoCommonService.commonOperator(PARTUPDATE, params, null, repositoryName, id);
		// 根据服务返回值，设置返回头error_code信息
		response.setHeader(ERROR_HEADER, errorCode);
	}

	/**
	 * 处理dao删除的请求.
	 * 
	 * @param response
	 *            http response
	 * @param repositoryName
	 *            访问的后台repository名字
	 * @param id
	 *            需要删除的id
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws BeansException
	 * @throws PermissionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@RequestMapping(value = "/repository/{repositoryName}/{id}", method = RequestMethod.DELETE, produces = FORMAT)
	public void doDelete(HttpServletResponse response, @PathVariable String repositoryName, @PathVariable Object id)
			throws BeansException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, PermissionException, NoSuchFieldException, SecurityException {
		String errorCode = (String) repoCommonService.commonOperator(DELETE, null, null, repositoryName, id);
		// 根据服务返回值，设置返回头error_code信息
		response.setHeader(ERROR_HEADER, errorCode);
	}

	/**
	 * 访问repository的方法 参数params=[]
	 * 
	 * @param request
	 * @param response
	 * @param repositoryName
	 * @param funcName
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 * @throws BeansException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/repository/{repositoryName}/{funcName}", method = RequestMethod.GET, produces = Constants.FORMAT)
	@Transactional(readOnly = true)
	public @ResponseBody String query(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String repositoryName, @PathVariable String funcName)
			throws JSONException, SQLException, BeansException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, PermissionException, ServiceException {
		String params = null;
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.get(Constants.PARAMS) != null) {
			params = paramMap.get(Constants.PARAMS)[0];
		}
		String result = repoQueryService.query(repositoryName, funcName, params, null);
		if (result.equals(ERROR_CODE_NO_SERVICE)) {
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		}
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return result;
	}

	/**
	 * 访问repository的方法,参数key=value&k1=v1
	 * 
	 * @param request
	 * @param response
	 * @param repositoryName
	 * @param funcName
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 * @throws BeansException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/repository/key/{repositoryName}/{funcName}", method = RequestMethod.GET, produces = Constants.FORMAT)
	@Transactional(readOnly = true)
	public @ResponseBody String queryKey(HttpServletRequest request, HttpServletResponse response,@PathVariable String repositoryName, @PathVariable String funcName)
			throws JSONException, SQLException, BeansException, IllegalAccessException, IllegalArgumentException,InvocationTargetException, PermissionException, ServiceException {
		String result = repoQueryService.query(repositoryName, funcName, request.getParameterMap(), null);
		if (result.equals(ERROR_CODE_NO_SERVICE)) {
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		}
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return result;
	}

	/**
	 * 分页访问repository的方法 参数params=[]
	 * 
	 * @param request
	 * @param response
	 * @param repositoryName
	 * @param funcName
	 * @param page
	 * @param size
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 * @throws BeansException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/repository/{repositoryName}/{funcName}/{page}/{size}", method = RequestMethod.GET, produces = Constants.FORMAT)
	@Transactional(readOnly = true)
	public @ResponseBody String queryByPage(HttpServletRequest request, HttpServletResponse response,@PathVariable String repositoryName, @PathVariable String funcName, @PathVariable int page,@PathVariable int size) 
					throws JSONException, SQLException, BeansException, IllegalAccessException,IllegalArgumentException, InvocationTargetException, PermissionException, ServiceException {
		// 处理分页情况
		String[] sorts = request.getParameterValues(Constants.SORT);
		Pageable pageable = Helper.convert2Pageable(sorts, page, size);
		String params = null;
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.get(Constants.PARAMS) != null) { 
			params = paramMap.get(Constants.PARAMS)[0];
		}

		String result = repoQueryService.query(repositoryName, funcName, params, pageable);

		if (result.equals(ERROR_CODE_NO_SERVICE)) {
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		}
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return result;
	}

	/**
	 * 分页访问repository的方法 参数key=value&k1=v1
	 * 
	 * @param request
	 * @param response
	 * @param repositoryName
	 * @param funcName
	 * @param page
	 * @param size
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 * @throws BeansException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws PermissionException
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/repository/key/{repositoryName}/{funcName}/{page}/{size}", method = RequestMethod.GET, produces = Constants.FORMAT)
	@Transactional(readOnly = true)
	public @ResponseBody String queryKeyByPage(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String repositoryName, @PathVariable String funcName, @PathVariable int page,
			@PathVariable int size) throws JSONException, SQLException, BeansException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, PermissionException, ServiceException {
		// 处理分页情况
		String[] sorts = request.getParameterValues(Constants.SORT);
		Pageable pageable = Helper.convert2Pageable(sorts, page, size);

		String result = repoQueryService.query(repositoryName, funcName, request.getParameterMap(), pageable);

		if (result.equals(ERROR_CODE_NO_SERVICE)) {
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		}
		// 设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return result;
	}

}
