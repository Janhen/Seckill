package com.janhen.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 安全性保障机制
 */
public class MD5Util {
	
	private static final String SALT = "1a2b3c4d";
	
	/** C0. 核心方法 */
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}
	
	/** C1. 一次加密*/
	public static String inputPassToFormPass(String input) {
		String temp = "" + SALT.charAt(0) + SALT.charAt(2) + 
				input + SALT.charAt(5) + SALT.charAt(4);
		return md5(temp);
	}
	
	/** C1. 二次加密*/
	public static String formPassToDBPass(String form, String dbSalt) {
		String str = ""+dbSalt.charAt(0)+dbSalt.charAt(2) + form +dbSalt.charAt(5) + dbSalt.charAt(4);
		return md5(str);
	}
	
	/** C1. 合并加密 */
	public static String inputPassToDBPass(String input, String dbSalt) {
		String formPass = inputPassToFormPass(input);
		String dbPass = formPassToDBPass(formPass, dbSalt);
		
		return dbPass;
	}
}
























