package com.janhen.seckill.vo.form;

import com.janhen.seckill.controller.common.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class LoginForm {
	
//	@Pattern(regexp="1\\d{10}")
	@IsMobile
	private String mobile;
	
	@NotNull
	@Size(min=6)
	private String password;
}
