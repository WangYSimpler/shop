package com.gofirst.framework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取网络文件mime处理类型
 * 
 */
public class MIME {

	private static Map<String, String> mimeMap = new HashMap<String, String>();

	static {
		mimeMap.put("doc", "application/msword");
		mimeMap.put("docx", "application/x-msword");
		mimeMap.put("xls", "application/vnd.ms-excel");
		mimeMap.put("xlsx", "application/vnd.ms-excel");
		mimeMap.put("ppt", "application/vnd.ms-powerpoint");
		mimeMap.put("pptx", "application/vnd.ms-powerpoint");
		mimeMap.put("pdf", "application/pdf");
		mimeMap.put("jpg", "image/jpeg");
		mimeMap.put("bmp", "image/bmp");
		mimeMap.put("gif", "image/gif");
		mimeMap.put("png", "image/png");
	}

	/**获取MIME type
	 * @param fileType
	 * @return
	 */
	public static String getMime(String fileType) {
		if(mimeMap.get(fileType) == null) {
			return "application/octet-stream";
		} else {
			return mimeMap.get(fileType);
		}
	}
}
