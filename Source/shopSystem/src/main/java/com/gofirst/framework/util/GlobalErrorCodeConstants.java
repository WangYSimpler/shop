package com.gofirst.framework.util;

/**
 * 访问全局错误码
 *
 */
public class GlobalErrorCodeConstants {
	
	public static final String ERROR_HEADER = "error_code";
	/**
	 * 正常
	 */
	public static final String ERROR_CODE_SUCCESS = "200";
	/**
	 * 未登录
	 */
	public static final String ERROR_CODE_NOT_LOGIN = "100";
	/**
	 * 无权限访问
	 */
	public static final String ERROR_CODE_NO_ACCESS = "101";
	/**
	 * 访问版本不支持
	 */
	public static final String ERROR_CODE_VERSION = "102";
	/**
	 * 会话失效
	 */
	public static final String ERROR_CODE_SESSION_INVALID = "103";
	/**
	 * 请求无对应服务
	 */
	public static final String ERROR_CODE_NO_SERVICE = "104";
	/**
	 * 请求接口错误
	 */
	public static final String ERROR_CODE_INTERFACE = "105";
	/**
	 * 返回对象无法做json对象转换
	 */
	public static final String ERROR_CODE_JSON = "106";
	/**
	 * 请求参数个数错误
	 */
	public static final String ERROR_CODE_REQUEST = "107";
	/**
	 * 请求参数类型错误
	 */
	public static final String ERROR_CODE_PARAM = "108";
	/**
	 *请求参数无法转换java对象
	 */
	public static final String ERROR_CODE_PARAM_JSON = "109";
	/**
	 * 后台业务处理遇到未知错误
	 */
	public static final String ERROR_CODE_UNKNOWN = "110";
	/**
	 * entity定义错误，没有主键Id
	 */
	public static final String ERROR_CODE_ID = "111";
	/**
	 * 数据库无法获得连接
	 */
	public static final String ERROR_CODE_DB = "112";
	/**
	 * 参数验证错误
	 */
	public static final String ERROR_CODE_PARAM_VALID = "113";
	
	
}
