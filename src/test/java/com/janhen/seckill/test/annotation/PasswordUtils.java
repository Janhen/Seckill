package com.janhen.seckill.test.annotation;

import java.util.List;

public class PasswordUtils {
	
	public static final String PATTERN_ROLE = "\\w*\\d\\w*";
	
	@UseCase(id = 15, description = "password must contain at least one numeric")
	public boolean validatePassword(String password) {
		return password.matches(PATTERN_ROLE);
	}
	
	@UseCase(id = 16)
	public String encryptPassword(String password) {
		return new StringBuilder(password).reverse().toString();
	}

	@UseCase(id = 52, description = "New passwords can't equal previously userd ones")
	public boolean checkForNewPassword( List<String> prevPasswords, String password) {
			
		boolean isContain = prevPasswords.contains(password);
		return !isContain;
	}

}
