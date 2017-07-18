package com.gofirst.framework.control;

import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_NO_SERVICE;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.gofirst.framework.util.GlobalErrorCodeConstants.ERROR_HEADER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.gofirst.framework.response.ResponseResult;
import com.gofirst.framework.util.Constants;
import com.gofirst.framework.util.Helper;
//import com.google.gson.Gson;

@Controller
public class UploadController {
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger
			.getLogger(UploadController.class);
	
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
	
	@RequestMapping(value="/upload/{serviceName}/{funcName}", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String upload(@RequestParam(value = "file",required=true)MultipartFile files, HttpServletRequest request, HttpServletResponse response,
				@PathVariable String serviceName, @PathVariable String funcName) throws JSONException {
		ResponseResult result = new ResponseResult();
		// 得到对应service名字的服务
		Object uploadService = context.getBean(serviceName);
		// 根据方法名字反射到接口中的方法
		List<Method> methodList = Helper.getAllInterfaceMethods(uploadService);
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
			try {
				objects = Helper.json2ObjectArray( array, method,paramCountFromReq, null, hasSessionAttributeAnnotation);
				List<File> fileList = getUploadFile(request);
				Object[] newObjects;
				if (objects != null) {
					newObjects = addElementInArray(objects, fileList);
				} else {
					newObjects = new Object[1];
					newObjects[0] = fileList;
				}
				// 反射调用上传文件的方法
				Object data = method.invoke(uploadService, newObjects);
				//result.setData(gson.toJson(data));
				result.setData(JSON.toJSONString(data));
			}  catch (IOException e) {
				logger.info("上传文件出现错误 " + e.getMessage());
				result.setErrorCode(-1);
				result.setErrorMsg("上传文件出现错误");
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				logger.info("uploadController调用上传服务出现错误 " + e.getMessage());
				result.setErrorCode(-1);
				result.setErrorMsg("uploadController调用上传服务出现错误");
			}
		}
		// 检查服务中是否有对应的方法
		if (!exist) {
			logger.info("请求无对应服务");
			response.setHeader(ERROR_HEADER, ERROR_CODE_NO_SERVICE);
			return null;
		}
		//设置成功的返回头
		response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
		return JSON.toJSONString(result);
	}
	
	private List<File> getUploadFile(HttpServletRequest request) throws IOException {
		List<File> fileList = new ArrayList<File>();
		MultipartHttpServletRequest mRequest = null;
		try{
			mRequest = (MultipartHttpServletRequest)request;
		} catch(Exception e) {
			e.printStackTrace();
		}
		Map<String, MultipartFile> fileMap = mRequest.getFileMap();
		String fileName = null;
		for(Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, MultipartFile> entry = it.next();
			MultipartFile mFile =entry.getValue();
			fileName =new String(mFile.getOriginalFilename().getBytes("ISO8859-1"),"UTF-8");
			
			//构建临时文件
			File tmpFile = File.createTempFile(fileName.split(Constants.PUNCTUATION)[0],
					"."+ fileName.split(Constants.PUNCTUATION)[1]);
			FileOutputStream outputStream = new FileOutputStream(tmpFile);
			FileCopyUtils.copy(mFile.getInputStream(), outputStream);
			fileList.add(tmpFile);
		}
		return fileList;
		
	}
	
	private Object[] addElementInArray(Object[] objects, Object newObj) {
		Object[] newObjects = new Object[objects.length + 1];
		//新元素放在新的数组第一个
		newObjects[0] = newObj;
		int i = 1;
		for (Object oldObj : objects) {
			newObjects[i] = oldObj;
			i++;
		}
		return newObjects;
	}

}
