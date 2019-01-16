package com.janhen.seckill.vo.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {
	
	@Pattern(regexp="1\\d{10}")
	private String mobile;
	
	@NotNull
	@Size(min=6)
	private String password;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserService [mobile=" + mobile + ", password=" + password + "]";
	}
	

}
