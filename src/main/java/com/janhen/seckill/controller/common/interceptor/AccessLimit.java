package com.janhen.seckill.controller.common.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface AccessLimit {

  int seconds() default -1;

  int maxCount() default -1;

  boolean needLogin() default true;
}
