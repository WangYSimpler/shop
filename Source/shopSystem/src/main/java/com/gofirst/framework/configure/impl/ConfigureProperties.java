package com.gofirst.framework.configure.impl;

import static com.gofirst.framework.util.Constants.CONFIG_FILE;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.inject.Named;

import org.apache.log4j.Logger;

import com.gofirst.framework.configure.Configuration;

/**
 * 读取configure.properties配置文件
 * 
 */
@Named
public class ConfigureProperties implements Configuration {

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
	 * @throws Exception
	 */
	public ConfigureProperties() {
		try {
			properties.load(ConfigureProperties.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (Exception e) {
			logger.info("读取配置文件" + CONFIG_FILE + "失败！");
			throw new RuntimeException("读取配置文件" + CONFIG_FILE + "失败！");
		}
	}

	/**
	 * 获取属性
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
		String value = null;
		try {
			value = new String(properties.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}

		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

}
