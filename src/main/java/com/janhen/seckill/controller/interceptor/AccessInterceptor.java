package com.janhen.seckill.controller.interceptor;

import com.janhen.seckill.common.Const;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.AccessKey;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.impl.SeckillUserServiceImpl;
import com.janhen.seckill.util.CookieUtil;
import com.janhen.seckill.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

		SeckillUser user = getUserByToken(request, response);
		UserContext.setUser(user);

		log.info("【】【】");
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			String methodName = handlerMethod.getMethod().getName();
			String className = handlerMethod.getBean().getClass().getName();

			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				log.info("无访问控制");
				return true;
			}

			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			log.info("访问控制参数: needLogin: {}, seconds: {}, maxCount: {}", needLogin, seconds, maxCount);
			if (needLogin) {
				if (user == null) {
					// put error message into response cache
					log.error("【访问控制】需要登录");
					WebUtil.render(response, ResultEnum.SESSION_ERROR);
					return false;
				}
			}

			AccessKey accessKeyPrefix = AccessKey.createByExpire(seconds);
			String key = request.getRequestURI() + Const.SPLIT + user.getId();

			Integer curAccessCnt = redisService.get(accessKeyPrefix, key, Integer.class);
			log.info("访问 Key: {}", curAccessCnt);
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
		return true;
	}

	private SeckillUser getUserByToken(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(Const.COOKIE_NAME_TOKEN);
		String cookieToken = CookieUtil.readLoginToken(request);
		if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		return userService.getByToken(response, token);
	}
}
