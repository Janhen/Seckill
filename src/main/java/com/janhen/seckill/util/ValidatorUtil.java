package com.janhen.seckill.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;


/**
 * 验证工具类
 */
public class ValidatorUtil {
	
	private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");
	private static final String ROLE = "1\\d{10}";
			
	public static boolean isMobile(String src) {
		if (StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher matcher = MOBILE_PATTERN.matcher(src);
		return matcher.matches();
	}
	
	public static boolean isMobileV2(String src) {
		if (src == null || src.length() == 0) { return false; }
		
		return Pattern.matches(ROLE, src);
	} 
	
	
	/*public static void main(String[] args) {
		System.out.println( isMobileV2("1515151515151515") );
		System.out.println( isMobileV2("15258656236") );
	}*/

}
