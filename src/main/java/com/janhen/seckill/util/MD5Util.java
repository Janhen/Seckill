package com.janhen.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	private static final String SALT = "1a2b3c4d";
	
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}
	
	/** first encryption: form ⇒ server, for test. */
	public static String inputPassToFormPass(String input) {
		String temp = "" + SALT.charAt(0) + SALT.charAt(2) + 
				input + SALT.charAt(5) + SALT.charAt(4);
		return md5(temp);
	}
	
	/** second encryption: server ⇒ db. */
	public static String formPassToDBPass(String form, String dbSalt) {
		String str = ""+dbSalt.charAt(0)+dbSalt.charAt(2) + form +dbSalt.charAt(5) + dbSalt.charAt(4);
		return md5(str);
	}
	
	/** twice encryption, for test. */
	public static String inputPassToDBPass(String input, String dbSalt) {
		String formPass = inputPassToFormPass(input);
		String dbPass = formPassToDBPass(formPass, dbSalt);
		return dbPass;
	}
}
























