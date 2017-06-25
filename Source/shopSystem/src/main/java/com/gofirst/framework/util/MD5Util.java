package com.gofirst.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * MD5
 *
 */
public class MD5Util {

	/*
	 * @param plainText
	 * 
	 * @return
	 */
	public static String md5(String plainText) {
		
		if (null == plainText) {
			plainText = "";
		}
		String MD5Str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuilder builder = new StringBuilder(32);
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					builder.append("0");
				builder.append(Integer.toHexString(i));
			}
			MD5Str = builder.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return MD5Str;
	}

	public static void main(String[] args)  {
	
		String md = MD5Util.md5("1");
		System.out.println(md);
	}
	
}
