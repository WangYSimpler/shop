package com.gofirst.framework.configure;

import static com.gofirst.framework.util.Constants.FRAMEWORK_CONFIG_FILE;

import java.util.Properties;

import javax.inject.Named;

import org.apache.log4j.Logger;

/**
 * 读取frameworkConfig.properties配置文件
 * 
 */
@Named
public class SystemConfigProperties implements Configuration {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(ConfigureProperties.class);

	/**
	 * 配置项
	 */
	private Properties properties = new Properties();

	/**
	 * 构造
	 * 
	 * @throws Exception
	 */
	public SystemConfigProperties() {
		try {
			properties.load(ConfigureProperties.class.getClassLoader().getResourceAsStream(FRAMEWORK_CONFIG_FILE));
			
		} catch (Exception e) {
			logger.info("读取配置文件" + FRAMEWORK_CONFIG_FILE + "失败！");
			throw new RuntimeException("读取配置文件" + FRAMEWORK_CONFIG_FILE + "失败！");
		}
	}
	
	/**
	 * 获取属性
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 获取属性,如果没有则返回默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@Override
	public String getProperty(String key, String defaultValue) {
		String value = properties.getProperty(key);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}
	
}
