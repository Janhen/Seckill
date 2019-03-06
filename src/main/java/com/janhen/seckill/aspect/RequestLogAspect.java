package com.janhen.seckill.aspect;

import com.janhen.seckill.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
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

    @Pointcut("execution(* com.janhen.seckill.controller..*Controller.*(..))")
    public void requestLog() {}

    @Before("requestLog()")
    public void beforeMethod(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuilder sb = new StringBuilder();
        log.info("Param: {} ", request.getParameterMap());
        log.info("URL: {}", request.getRequestURL());
        log.info("IP: {}", request.getRemoteAddr().toString());
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append("arg: " + arg.toString() + "|");
            }
        }
        log.info("方法参数: before method: {}", sb);
    }


    @AfterReturning(returning = "o", pointcut = "requestLog()")
    public void afterMethod(Object o) {
        log.info("Response Data: {}", JSONUtil.beanToString(o));
    }
}
