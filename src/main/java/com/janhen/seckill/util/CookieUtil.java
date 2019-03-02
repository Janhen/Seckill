package com.janhen.seckill.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    public static final String COOKIE_DOMAIN = "";

    public static final String COOKIE_NAME = "token";

    // login

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (COOKIE_NAME.equals(ck.getName())) {
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        // ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");
        ck.setHttpOnly(true);
        ck.setMaxAge(30 * 60);
        log.info("set cookie, name:{}, value:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (COOKIE_NAME.equals(ck.getName())) {
                    // ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setHttpOnly(true);
                    ck.setMaxAge(0);
                    log.info("delete cooke,name:{},value:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                }
            }
        }
    }

    // other
    public static Cookie get(HttpServletRequest request, String name) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (ck.getName().equals(name)) {
                    return ck;
                }
            }
        }
        return null;
    }

    public static void set(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie ck = new Cookie(name, value);
        // ck.setDomain("");
        ck.setPath("/");
        ck.setHttpOnly(true);
        ck.setMaxAge(maxAge);
        response.addCookie(ck);
    }
}
