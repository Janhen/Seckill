package com.janhen.seckill.util;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
 
    private static ApplicationContext context = null;
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }
 
    // 传入线程中
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    // 传入线程中
    public static <T> T getBean(Class<T> beanName) {
        return (T) context.getBean(beanName);
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static String getProperty(String key) {
        return context.getEnvironment().getProperty(key);
    }

    public static String getHost() {
        return getProperty("eureka.instance.hostname");
    }

    public static String getPort() {
        return getProperty("server.port");
    }

    public static String getServerId() {
        return getProperty("spring.application.name");
    }

    public static String getDbIp() {
        String dbUrl = getProperty("spring.datasource.url");
        int lastPoint = dbUrl.lastIndexOf(":");
        int startPoint = dbUrl.indexOf("//");
        return dbUrl.substring(startPoint + 2, lastPoint);
    }
 
    // 国际化使用
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }
 
    /// 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }
}

