package com.gofirst.framework.util;

/**
 * 全局常量
 */

public class Constants {
	
	/**
	 * session_time_out
	 */
	public static final String SESSION_TIME_OUT = "session.timeout";
	/**
	 * realm bean name
	 */
	public static final String REALM_BEAN_NAME = "realm.bean.name";
	/**
	 * login call backs
	 */
	public static final String LOGIN_CALL_BACKS = "login.callback.names";
	/**
	 * config配置文件
	 */
	public static final String CONFIG_FILE = "configure.properties";
	/**
	 * hibernate建表策略
	 */
	public static final String HIBERNATE_STRATEGY = "hibernate.hbm2ddl.auto";
	/**
	 * 框架配置文件
	 */
	public static final String FRAMEWORK_CONFIG_FILE = "frameworkConfig.properties";
	
	public static final String HIBERNATE_DB_SUBJECT = "hibernate.db";
	
	
	public static final String BASE_PACKAGE = "basebackage.scan";
	public static final String JPA_BASE_PACKAGE = "jpa.basebackage.scan";
	/**
	 * 前台post传过来的body的key
	 */
	public static final String PARAMS = "params";
	/**
	 * 空字符串
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * CRUD
	 */
	public static final String	SAVE = "save";
	public static final String	DELETE = "delete";
	public static final String	FINDONE = "findOne";
	public static final String	FINDALL = "findAll";
	public static final String	COUNT = "count";
	public static final String	UPDATE = "update";
	public static final String	PARTUPDATE = "partUpdate";
	
	/**
	 * null 字符串
	 */
	public static final String NULL = "NULL";
	/**
	 * 查询所有数据时，后台数据超过2000报错
	 */
	public static final int GETALL_LIMIT = 2000;
	/**
	 * 分页，每页数据限制200条
	 */
	public static final int PAGE_LIMIT = 200;
	/**
	 * 分页查询时，排序参数名称
	 */
	public static final String SORT = "sort";
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	
	/**
	 * 动态sql正则表达式
	 */
	public static final String DYNAMIC_SQL_PATTERN = "for\\{.*?\\}|\\{.*?\\}";
	
	public static final String B_BRACKET = "{";
	public static final String E_BRACKET = "}";
	public static final String COMMA = ",";
	public static final String PUNCTUATION = "\\.";
	public static final String STAR = "*";
	public static final String REG_STAR = ".*";
	public static final String COLON = ":";
	public static final String QUESTION_MARK = "?";
	public static final String SQL_IN = " in ";
	public static final String QUESTION_MARK_PATTERN = "\\?";
	public static final String TOKEN = "token";
	public static final String EXPIRE_TIME = "expire_time";
	///用户相关
	public static final String USERNAME = "userName";
	public static final String USERNO = "userNo";
	public static final String PASSWORD = "password";
	public static final String TYPE = "type"; 
	
	///数据持久化
	public static final String SERVICE = "service";
	public static final String DAO = "dao";
	/**
	 * 服务中Session的属性名
	 */
	public static final String SESSION = "session";
	/**
	 * 斜杠
	 */
	public static final String SLASH = "/";
	public static final String AT = "@";
	public static final String POUND = "#";
	/**
	 * controller返回的格式
	 */
	public static final String FORMAT = "text/plain;charset=UTF-8";
	
	//public static final String ID = "id";
	
	public static final String SESSION_PERMISSION = "session_permission";
	public static final String SESSION_REMOTE = "session.remote";
	public static final String REMOTE = "remote";
	public static final String REDISSESSIONDAO = "redisSessionDAO";
	public static final String REDISMANAGER = "redisManager";
	public static final String REDIS_HOST = "redisManager.host";
	public static final String REDIS_PORT = "redisManager.port";
	public static final String REDIS_EXPIRE = "redisManager.expire";
	
	public static final String NOSESSION = "noSession";
	public static final String NOSESSIONPERMISSIONS = "nosession.permission";
	public static final String SYSTEMNAME = "system.name";
	
}
