package com.janhen.seckill.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface AccessLimit {
	
	int seconds();
	
	int maxCount();
	
	boolean needLogin() default true;

}
