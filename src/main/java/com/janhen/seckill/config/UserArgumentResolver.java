package com.janhen.seckill.config;

import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.SeckillUserService;
import com.janhen.seckill.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分布式session的获得注入到方法参数中
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SeckillUserService userService;

    /**
     * handle method parameter:  SeckillUser
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        Class<?> parameterType = parameter.getParameterType();
        return parameterType.equals(SeckillUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        // take token from {request param, cookie}
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = WebUtil.getCookie(request, SeckillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }

        // priority: from request param > from cookie
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        // ★ 自动更新Cookie expire
        return userService.getByToken(response, token);
    }
}

