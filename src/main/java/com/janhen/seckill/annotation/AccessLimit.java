package com.janhen.seckill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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

  /**
   * need token
   */
  boolean needLogin() default true;

  /**
   * for limit
   */
  int seconds() default -1;

  int maxCount() default -1;
}
