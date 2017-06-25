package com.gofirst.framework.configure;

public interface Configuration {

	/**
	 * 获取属性
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key);
	
	/**
	 * 获取属性,如果没有则返回默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue);
	
}
