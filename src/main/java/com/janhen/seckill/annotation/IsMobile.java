package com.janhen.seckill.annotation;

import com.janhen.seckill.controller.common.validator.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验
 *
 * @author janhen
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {
  boolean required() default true;

  String message() default "手机号码格式错误";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
