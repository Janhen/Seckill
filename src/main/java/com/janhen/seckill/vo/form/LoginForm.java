package com.janhen.seckill.vo.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginForm {
	
	@Pattern(regexp="1\\d{10}")
	private String mobile;
	
	@NotNull
	@Size(min=6)
	private String password;
}
