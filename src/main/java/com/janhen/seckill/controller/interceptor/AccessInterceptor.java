package com.janhen.seckill.controller.interceptor;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.AccessKey;
import com.janhen.seckill.common.redis.key.BasePrefix;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.impl.SeckillUserServiceImpl;
import com.janhen.seckill.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter{

	@Autowired
    SeckillUserServiceImpl userService;
	
	@Autowired
    RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			String methodName = handlerMethod.getMethod().getName();
			String className = handlerMethod.getBean().getClass().getName();

			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				return true;
			}

			// handle limit

			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			SeckillUser user = UserContext.getUser();
			if (needLogin) {
				if (user == null) {
					log.error("【访问控制】需要登录");
					WebUtil.render(response, ResultEnum.SESSION_ERROR);
					return false;
				}
			}
			if (seconds != -1 && maxCount != -1) {
				AccessKey accessKeyPrefix = AccessKey.createByExpire(seconds);
				String key = BasePrefix.getKey(request.getRequestURI(), user.getId());
				Integer curAccessCnt = redisService.get(accessKeyPrefix, key, Integer.class);
				if (curAccessCnt == null) {
					redisService.set(accessKeyPrefix, key, 1);
				} else if (curAccessCnt < maxCount) {
					redisService.incr(accessKeyPrefix, key);
				} else {
					log.error("【访问控制】用户:{}, 访问 {}.{}过于频繁", user.getId(), className, methodName);
					WebUtil.render(response, ResultEnum.ACCESS_LIMIT_REACHED);
					return false;
				}
			}
		}
		return true;
	}
}
