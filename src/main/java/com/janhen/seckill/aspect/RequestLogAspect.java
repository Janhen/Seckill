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

@Aspect
@Slf4j
@Component
public class RequestLogAspect {

    @Pointcut("execution(public com.janhen.seckill.controller.*Controller.*(..))")
    public void requestLog() {}

    @Before("requestLog()")
    public void beforeMethod(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        StringBuilder sb = new StringBuilder();
        log.info("URL: ", request.getRequestURL());
        log.info("IP: ", request.getRemoteAddr());
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append("arg: " + arg.toString() + "|");
            }
        }
        log.info("方法参数: before method: {}", sb);
    }
}
