package com.janhen.seckill.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求日志
 *
 * @janhen
 */
@Aspect
@Slf4j
@Component
public class RequestLogAspect {

  @Pointcut("execution(* com.janhen.seckill.controller..*Controller.*(..))")
  public void requestLog() {
  }

  @Before("requestLog()")
  public void beforeMethod(JoinPoint joinPoint) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    log.info("URL: {}", request.getRequestURL());
    log.info("IP: {}", request.getRemoteAddr());
  }
}
