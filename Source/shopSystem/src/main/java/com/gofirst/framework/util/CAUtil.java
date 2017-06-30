package com.gofirst.framework.util;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.gofirst.framework.exception.ServiceException;

/**
 * 数字证书工具类
 * @version 1.0
 */
public abstract class CAUtil {
	/**
	 * 日志记录
	 */
	private static Logger logger = Logger.getLogger(CAUtil.class);
	/**
	 * 证书存放Key
	 */
	public final static String CA_CERT_ATTRIBUTE = "javax.servlet.request.X509Certificate";
	/**
	 * 用户名字段名称
	 */
	public final static String CA_USERNAME_KEY = "CN=";
	/**
	 * 模块信息字段名称
	 */
	public final static String CA_MODULE_KEY = "OU=";
	/**
	 * 证书用户信息分隔符
	 */
	public final static String CA_SPLITER_SIGN = ",";
	
	private static Map < Integer, String > errMsgMapper = new HashMap<Integer, String> (0);
	/**
	 * 常量类中错误号字段前缀
	 */
	private static String ERRCODE_PREFIX = "ERRCODE";
	/**
	 * 常量类中错误信息字段前缀
	 */
	private static String ERRMSG_PREFIX = "ERRMSG";
	
	/**
	 * 从证书用户信息中获取用户名信息
	 * @param principal
	 * @return
	 * @throws ServiceException 
	 */
	public static String getUserName(X500Principal principal) throws ServiceException {
		AssertUtil.notNull(principal);
		AssertUtil.notEmpty(principal.getName());
		String princepalInfo;
		princepalInfo = principal.toString();
		String result = null;
		if ( !StringUtils.isEmpty(princepalInfo) ) {
			int keyPos = princepalInfo.indexOf(CA_USERNAME_KEY);
			if ( -1 != keyPos ) {
				keyPos += CA_USERNAME_KEY.length();
				result = princepalInfo.substring(keyPos);
				int firstSplitterPos = result.indexOf(CA_SPLITER_SIGN);
				if ( -1 != firstSplitterPos ) 
					result = result.substring(0, firstSplitterPos);
			} else {
				result = "";
			}
		}
		return result;
	}
	
	/**
	 * 从证书中获取序列号
	 * @param certificate
	 * @return
	 * @throws ServiceException 
	 */
	public static String getSerialNo(X509Certificate certificate) throws ServiceException {
		AssertUtil.notNull(certificate);
		BigInteger serialNoInt = certificate.getSerialNumber();
		return (null==serialNoInt) ? "" : serialNoInt.toString();
	}
	
	/**
	 * 从证书所有者信息中获取Module信息
	 * @param principal
	 * @return
	 * @throws ServiceException 
	 */
	public static String getModuleId(X500Principal principal) throws ServiceException {
		AssertUtil.notNull(principal);
		AssertUtil.notEmpty(principal.getName());
		String princepalInfo = principal.toString();
		String result = null;
		if ( !StringUtils.isEmpty(princepalInfo) ) {
			int keyPos = princepalInfo.indexOf(CA_MODULE_KEY);
			if ( -1 != keyPos ) {
				keyPos += CA_MODULE_KEY.length();
				result = princepalInfo.substring(keyPos);
				int firstSplitterPos = result.indexOf(CA_SPLITER_SIGN);
				if ( -1 != firstSplitterPos ) 
					result = result.substring(0, firstSplitterPos);
			} else {
				result = "";
			}
		}
		//TODO delete after key changed, since current key has a test prefix of the moduleid
		if ( !StringUtils.isEmpty(result) ) {
			if ( result.startsWith("test") ) {
				result = result.substring(4);
			}
		}
		//TODO end
		return result;
	}
	
	/**
	 * 从Request中获取X509证书
	 * @param request
	 * @return
	 */
	public static X509Certificate getCertificate(HttpServletRequest request) {
		if ( logger.isDebugEnabled() )
			logger.debug("");
		boolean isSecure = false;
		X509Certificate result = null;
		X509Certificate[] results = null;
		if ( null != request ) {
			isSecure = request.isSecure();
		}
		if ( isSecure ) {
			if ( isMultiCertificate(request) ) {
				results = (X509Certificate[]) request.getAttribute(CA_CERT_ATTRIBUTE);
				result = results[0];
			} else {
				result = (X509Certificate) request.getAttribute(CA_CERT_ATTRIBUTE);
			}
		} 
		if ( logger.isDebugEnabled() )
			logger.debug("");
		return result;
	}
	
	/**
	 * 获取多份数字证书
	 * @param request HttpServletRequest
	 * @return X509Certificate[]
	 */
	public static X509Certificate[] getCertificates(HttpServletRequest request) {
		if ( logger.isDebugEnabled() )
			logger.debug("");
		boolean isSecure = false;
		X509Certificate result = null;
		X509Certificate[] results = null;
		if ( null != request ) {
			isSecure = request.isSecure();
		}
		if ( isSecure ) {
			if ( isMultiCertificate(request) ) {
				results = (X509Certificate[]) request.getAttribute(CA_CERT_ATTRIBUTE);
			} else {
				result = (X509Certificate) request.getAttribute(CA_CERT_ATTRIBUTE);
				results = new X509Certificate[1];
				results[0] = result;
			}
		} 
		if ( logger.isDebugEnabled() )
			logger.debug("");
		return results;
	}
	
	/**
	 * 判断是否含有多份证书
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	public static boolean isMultiCertificate(HttpServletRequest request) {
		boolean isSecure = false;
		boolean result = false;
		if ( null != request ) {
			isSecure = request.isSecure();
		}
		if ( isSecure ) 
			result = request.getAttribute(CA_CERT_ATTRIBUTE).getClass().isArray();
		return result;
	}
	
	/**
	 * 根据错误号,获取错误信息
	 * @param errCode int 错误号
	 * @return String 对应错误信息,如果系统未定义该错误号,则返回默认错误信息
	 * @see CAUtil.CAConstant
	 * @see CAUtil#buildErrMsgMapper
	 */
	public static String getErrMsg(final int errCode) {
		String errMsg = null;
		if ( CollectionUtils.isEmpty(errMsgMapper) ) {
			buildErrMsgMapper();
		}
		errMsg = errMsgMapper.get(Integer.valueOf(errCode));
		if ( StringUtils.isEmpty(errMsg) )
			errMsg = CAConstant.ERRMSG_DEFAULT_ERRMSG;
		return errMsg;
	}
	
	/**
	 * 使用Java反射机制,初始化错误号与错误信息对应关系.
	 * @see CAUtil.CAConstant
	 */
	private static void buildErrMsgMapper() {
		if ( null == errMsgMapper )
			errMsgMapper = new HashMap<Integer, String>(0);
		Class<?> constantClass = CAConstant.class;
		for ( Field field : constantClass.getDeclaredFields() ) {
			String fieldName = field.getName();
			if ( !StringUtils.isEmpty(fieldName) ) {
				if ( fieldName.startsWith(ERRCODE_PREFIX) ) {
					try {
						int errCode = field.getInt(null);
						String errName = fieldName.substring(ERRCODE_PREFIX.length());
						String errMsgFieldName = ERRMSG_PREFIX + errName;
						Field errMsgField = constantClass.getDeclaredField(errMsgFieldName);
						if ( null != errMsgField ) {
							String errMsg = (String) errMsgField.get(null);
							errMsgMapper.put(Integer.valueOf(errCode), errMsg);
						}
					} catch (IllegalArgumentException e) {
						logger.error("尝试初始化证书认证错误信息时发生错误, 错误字段名称: " + fieldName, e);
					} catch (IllegalAccessException e) {
						logger.error("尝试初始化证书认证错误信息时发生错误, 错误字段名称: " + fieldName, e);
					} catch (SecurityException e) {
						logger.error("尝试初始化证书认证错误信息时发生错误, 错误字段名称: " + fieldName, e);
					} catch (NoSuchFieldException e) {
						logger.error("尝试初始化证书认证错误信息时发生错误, 错误字段名称: " + fieldName, e);
					}
				}
			}
		}
	}
	
	/**
	 * CA认证常量定义内部类.主要用于定义CA认证时的错误号以及错误信息.<br/>
	 * 错误号以{@link CAUtil#ERRCODE_PREFIX ERRCODE}为前缀,错误信息以{@link CAUtil#ERRMSG_PREFIX ERRMSG}为前缀.
	 * <br/>
	 * 错误号为int类型,错误类型为String类型<br/>
	 * 当定义一个错误号后,应定义一个相同名称的错误信息.否则当程序根据错误号查询错误信息时,<br/>
	 * 将会因无法找到对应错误信息而返回{@link CAConstant#ERRMSG_DEFAULT_ERRMSG 默认的错误信息}
	 * @author Ryan Huang
	 * @version 1.0
	 * @see CAUtil
	 */
	public static class CAConstant {
		/**
		 * 验证成功
		 */
		public final static int ERRCODE_SUCCESS = 0;
		/**
		 * 验证失败
		 */
		public final static int ERRCODE_NOCERTIFICATE = -1;
		public final static int ERRCODE_CERTIFICATE_EXPIRED = -2;
		public final static int ERRCODE_CERTIFICATE_NOT_YET_VALID = -3;
		public final static int ERRCODE_CERTIFICATE_WRONG_MODULEINFO = -4;
		public final static int ERRCODE_CERTIFICATE_NO_ISSUER = -5;
		public final static int ERRCODE_CERTIFICATE_WRONG_ISSUER = -6;
		
		/**
		 * CA认证默认错误信息, 当程序无法查询到对应错误号的错误信息时,将返回该信息.
		 */
		public final static String ERRMSG_DEFAULT_ERRMSG = "验证用户证书信息时发生未知错误";
		public final static String ERRMSG_SUCCESS = "验证用户证书成功";
		public final static String ERRMSG_NOCERTIFICATE = "未找到用户证书信息";
		public final static String ERRMSG_CERTIFICATE_EXPIRED = "用户证书已过期";
		public final static String ERRMSG_CERTIFICATE_NOT_YET_VALID = "用户证书未到使用期";
		public final static String ERRMSG_CERTIFICATE_WRONG_MODULEINFO = "用户无权限登陆该模块";
		public final static String ERRMSG_CERTIFICATE_NO_ISSUER = "证书中无发布者信息";
		public final static String ERRMSG_CERTIFICATE_WRONG_ISSUER = "证书发布者不符合系统使用证书发布者";
	}
}

