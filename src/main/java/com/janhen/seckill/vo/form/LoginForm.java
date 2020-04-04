package com.janhen.seckill.vo.form;

import com.janhen.seckill.util.PatternContants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
	@Pattern(regexp = PatternContants.PHONE_PATTERN)
	private String mobile;
	
	@NotNull
	@Size(min=6)
	private String password;
}
