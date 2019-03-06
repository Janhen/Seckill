package com.janhen.seckill.util;

import com.janhen.seckill.common.Const;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}
	
	/** first encryption: form ⇒ server, for test. */
	public static String inputPassToFormPass(String input) {
		String temp = "" + Const.SALT.charAt(0) + Const.SALT.charAt(2) +
				input + Const.SALT.charAt(5) + Const.SALT.charAt(4);
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

	public static void main(String[] args) {
		System.out.println(MD5Util.inputPassToDBPass("123456", "1a2b3c"));
	}
}
























