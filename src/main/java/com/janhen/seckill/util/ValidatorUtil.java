package com.janhen.seckill.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		if (src == null || src.length() == 0) {
			return false;
		}
		return Pattern.matches(ROLE, src);
	} 
}
