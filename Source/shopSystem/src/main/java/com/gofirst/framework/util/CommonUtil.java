package com.gofirst.framework.util;

public class CommonUtil {
	
	/**
	 * 判断是空串 或者 为null
	 * @return
	 */
	public static boolean isEmptyStr(String str)
	{
	  boolean flag = false;
	  if (str == null || str.equals("")) {
		  flag = true;
	  }
	  
	  return flag;
	}
	
}
