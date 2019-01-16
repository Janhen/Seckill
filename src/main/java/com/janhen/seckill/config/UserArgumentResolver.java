package com.janhen.seckill.config;

import com.janhen.seckill.interceptor.UserContext;
import com.janhen.seckill.pojo.SeckillUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 分布式session的获得注入到方法参数中
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType.equals(SeckillUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // Filter ⇒  DispatcherServlet  ⇒ Interceptor  ⇒  HandlerMethodArgumentResolver
        //                                    ^ set user to threadLocal
        return UserContext.getUser();
    }
}
















//        // 根据token 标识当前用户，返回用户信息
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//
//        // take token from {request param, cookie}
//        String paramToken = request.getParameter(SeckillUserServiceImpl.COOKIE_NAME_TOKEN);
//        String cookieToken = WebUtil.getCookie(request, SeckillUserServiceImpl.COOKIE_NAME_TOKEN);
//        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
//            return null;
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//
//        // ★★ 对每次认证访问, 自动更新Cookie expire
//        return userService.getByToken(response, token);

