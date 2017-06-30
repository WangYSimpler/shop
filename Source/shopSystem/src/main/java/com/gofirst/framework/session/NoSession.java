package com.gofirst.framework.session;

public interface NoSession {

	/**
	 * 获得所有不用登陆的服务
	 * @return
	 */
	String[] getNoSessions(String systemName);
	
}
