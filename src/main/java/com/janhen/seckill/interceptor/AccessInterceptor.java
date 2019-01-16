package com.janhen.seckill.interceptor;

import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.AccessKey;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.result.CodeMsg;
import com.janhen.seckill.service.SeckillUserService;
import com.janhen.seckill.util.WebUtil;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.result.CodeMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	SeckillUserService userService;
	
	@Autowired
    RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		SeckillUser user = getUserByToken(request, response);
		UserContext.setUser(user);
		if (handler instanceof HandlerMethod) {
			
			// get AccessLimit.class annotation
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) 
				return true;
			
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean isNeedLogin = accessLimit.needLogin();
			
			String key = request.getRequestURI();
			if (isNeedLogin) {
				if (user == null) {
					// put error message into response cache
					WebUtil.render(response, CodeMsg.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getId();
			} 
			
			// null point : String key = request.getRequestURI() + "_" + user.getId();
			AccessKey accessKeyPrefix = AccessKey.createByExpire(seconds);
			Integer accessCount = redisService.get(accessKeyPrefix, key, Integer.class);
			if (accessCount == null) {
				redisService.set(accessKeyPrefix, key, 1);
			} else if (accessCount < 5) {
				redisService.incr(accessKeyPrefix, key);
			} else {
				WebUtil.render(response, CodeMsg.ACCESS_LIMIT_REACHED);
				return false;
			}
		}
		return true;
	}
	
	private SeckillUser getUserByToken(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookie(request, SeckillUserService.COOKIE_NAME_TOKEN);
		
		if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) { return null; }
		
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		
		return userService.getByToken(response, token);
	}
	
	private String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}
}
