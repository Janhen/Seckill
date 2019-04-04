package com.janhen.seckill.controller.interceptor;

import com.janhen.seckill.common.Const;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.impl.SeckillUserServiceImpl;
import com.janhen.seckill.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    SeckillUserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        SeckillUser user = getUserByToken(request, response);
        UserContext.setUser(user);
        return true;
    }

    private SeckillUser getUserByToken(HttpServletRequest request, HttpServletResponse response) {
        // take from cookie OR URL rewrite
        String paramToken = request.getParameter(Const.COOKIE_NAME_TOKEN);
        String cookieToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserContext.deleteUser();
    }
}
