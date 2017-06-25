package com.gofirst.framework.session;

/**
 *
 * 自定义框架曾的session
 *
 */
public interface FrameworkSession {
	
	/**
	 * threadLocal to save current session id
	 */
	public static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
	/**
	 * 获得session中的attribute
	 * @param key	key
	 * @return	value
	 */
	public Object getAttribute(Object key);
	
	/**
	 * 设置session中的attribute
	 * @param key	key
	 * @param value	value
	 */
	public void setAttribute(Object key, Object value);
	
	/**
	 * 获得用户唯一标识
	 * @return	用户唯一标识
	 */
	public String getUserId();
	
	/**
	 * use threadlocal save session id by current thread 
	 * @param sessionId
	 */
	public static void setSessionId(String sessionId) {
		threadLocal.set(sessionId);
	}
	
	/**
	 * use threadlocal save session id by current thread 
	 * @param sessionId
	 */
	public static void removeSessionId() {
		threadLocal.remove();
	}
	
	/**
	 * get session id from threadlocal from current thread
	 * @return
	 */
	public static String getSessionId() {
		return threadLocal.get();
	}

}
