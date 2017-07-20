package com.gofirst.framework.control;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.response.ResponseResult;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;
//import com.google.gson.Gson;

@Controller
public class DownloadController {
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(DownloadController.class);
	
	/**
	 * spring context
	 */
	@Autowired(required = true)
	private ApplicationContext context;
	
	/**
	 * gson
	 */
	//@Autowired(required = true)
	//private Gson gson = null;

	@RequestMapping(value="/download/{serviceName}/{funcName}", method = RequestMethod.GET)
	public @ResponseBody
		void download(HttpServletRequest request, HttpServletResponse response,	@PathVariable String serviceName, @PathVariable String funcName)throws JSONException{
		ResponseResult result = new ResponseResult();
		// 得到对应下载service名字的服务
		Object downloadService = context.getBean(serviceName);
		// 根据方法名字反射到接口中的方法
		List<Method> methodList = Helper.getAllInterfaceMethods(downloadService);
		boolean exist = false;
		for (Method method : methodList) {
			if (!funcName.equals(method.getName())) {
				continue;
			}
			exist = true;
			JSONArray array = null;
			String params = request.getParameter(Constants.PARAMS);
			Object[] objects = null;
			int paramCountFromReq = 0;
			if (params != null) {
				array = new JSONArray().fluentAdd(params);
				paramCountFromReq = array.size();
			}
			//判断方法的参数是否含有@sessionAttribute
			boolean hasSessionAttributeAnnotation = Helper.hasSessionAttributeAnnotation(method);
			
			// 根据方法的参数类型，将json转换成java对象
			//objects = Helper.json2ObjectArray(gson, array, method,paramCountFromReq, null, hasSessionAttributeAnnotation);
			objects = Helper.json2ObjectArray( array, method,paramCountFromReq, null, hasSessionAttributeAnnotation);
			// 反射调用上传文件的方法
			File file;
			try {
				file = (File)method.invoke(downloadService, objects);
				FileInputStream in = new FileInputStream(file);
				response.setHeader("Content_Length", String.valueOf(in.available()));
				// 设置成功的返回头
				response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = in.read(bytes)) != -1) {
					response.getOutputStream().write(bytes, 0, len);
				}
				response.flushBuffer();
				in.close();
//				file.delete();
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				logger.info("downloadController调用下载服务出现错误");
				result.setErrorCode(-2);
				result.setErrorMsg("downloadController调用下载服务出现错误");
			} catch (FileNotFoundException e) {
				logger.info("文件没有找到");
				result.setErrorCode(-1);
				result.setErrorMsg("文件没有找到");
			} catch (IOException e) {
				logger.info("下载文件出现错误");
				result.setErrorCode(-2);
				result.setErrorMsg("下载文件出现错误");
			}
		}
		// 检查服务中是否有对应的方法
		if (!exist) {
			logger.info("请求无对应服务");
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return;
		}
		return;
	}
}
