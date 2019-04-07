package com.janhen.seckill.config;

import com.janhen.seckill.controller.common.UserArgumentResolver;
import com.janhen.seckill.controller.common.interceptor.AccessInterceptor;
import com.janhen.seckill.controller.common.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

	@Autowired
	UserArgumentResolver userArgumentResolver;
	
	@Autowired
	AccessInterceptor accessInterceptor;

	@Autowired
	SessionInterceptor sessionInterceptor;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(userArgumentResolver);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor);
		registry.addInterceptor(accessInterceptor);
	}
}
