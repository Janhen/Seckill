package com.janhen.seckill.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.janhen.seckill.util.ValidatorUtil;
import org.springframework.util.StringUtils;

import com.janhen.seckill.util.ValidatorUtil;


/**
 * JSR 303 ConstranintValidator
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String>{

	private boolean required = false;
	
	/**
	 * 获取注解值
	 */
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (required) {
			return ValidatorUtil.isMobile(value);
		} else {
			if (StringUtils.isEmpty(value)) {
				return true;
			} else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
