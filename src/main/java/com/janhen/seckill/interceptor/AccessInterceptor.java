package com.janhen.seckill.interceptor;

import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.AccessKey;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.service.impl.SeckillUserServiceImpl;
import com.janhen.seckill.util.CookieUtil;
import com.janhen.seckill.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
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
	/**
	 * 提取单个用户访问次数限制
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Http Request  ⇒  Filter  ⇒  DispatcherServlet  ⇒  HandlerInterceptor  ⇒  Controller
		//    Controller
		//      ⇒  afterPropertiesSet(need I InitializingBean) ⇒  `@PostConstructor` ???  ⇒  `@InitBinder` 初始化参数绑定??  ⇒  HandlerMethodArgumentResolver ??
		//                  ⇒  `@Valid、ModelAndView、@RequestParam`可默认获取参数 ??  ⇒  参数绑定(bean 映射...)  ⇒  method body
		SeckillUser user = getUserByToken(request, response);
		UserContext.setUser(user);

		if (handler instanceof HandlerMethod) {
			// get AccessLimit.class annotation
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			String methodName = handlerMethod.getMethod().getName();
			String className = handlerMethod.getBean().getClass().getName();
			if (accessLimit == null) {
				return true;
			}

			// ★★ 拦截带有 `@AccessLimit` 的方法
			//    - 登录控制
			//    - 单个用户访问带此注解次数限制,  目前非单个方法控制
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean isNeedLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if (isNeedLogin) {
				if (user == null) {
					// put error message into response cache
					log.error("【访问控制】需要登录");
					WebUtil.render(response, ResultEnum.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getId();        // can `+methodName` implement only one method access limit
			}

			AccessKey accessKeyPrefix = AccessKey.createByExpire(seconds);
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
		return true;
	}

	/**
	 * 从 Redis 获取session,并重置cookie、cache有效期
	 */
	private SeckillUser getUserByToken(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(SeckillUserServiceImpl.COOKIE_NAME_TOKEN);
		String cookieToken = CookieUtil.readLoginToken(request);
		if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		return userService.getByToken(response, token);
	}
}
