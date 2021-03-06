package com.janhen.seckill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 访问限制注解<br>
 *
 * @author janhen
 */
@Target({METHOD })
@Retention(RUNTIME)
public @interface AccessLimit {

  boolean needLogin() default true;

  int seconds() default -1;

  // limit
  long timeout() default 0L;

  TimeUnit timeUnit() default TimeUnit.SECONDS;

  int maxCount() default -1;
}
