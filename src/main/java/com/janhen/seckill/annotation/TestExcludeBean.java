package com.janhen.seckill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 排除测试时不需要创建的Bean
 *
 * @author Janhen
 */
@Target({TYPE, METHOD })
@Retention(RUNTIME)
public @interface TestExcludeBean {
}
