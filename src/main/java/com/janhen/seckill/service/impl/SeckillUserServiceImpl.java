package com.janhen.seckill.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.dao.SeckillUserMapper;
import com.janhen.seckill.exeception.SeckillException;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.redis.SeckillUserKey;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.util.CookieUtil;
import com.janhen.seckill.util.MD5Util;
import com.janhen.seckill.util.UUIDUtil;
import com.janhen.seckill.vo.form.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class SeckillUserServiceImpl implements ISeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    SeckillUserMapper seckillUserMapper;

    @Autowired
    RedisService redisService;

    public SeckillUser getById(long id) {
        // 1.take from cache
        SeckillUser seckillUser = redisService.get(SeckillUserKey.getById, "" + id, SeckillUser.class);
        if (seckillUser != null) {
            return seckillUser;
        }
        // 2.take from db and put cache
        seckillUser = seckillUserMapper.getById(id);
        if (seckillUser != null) {
            redisService.set(SeckillUserKey.getById, "" + id, seckillUser);
        }
        return seckillUser;
    }

    /**
     * 分布式 session ： 从 redis 中获得带有 expires 的token
     * 每次访问自动更新 Cookie 过期时间
     */
    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        // take from cache and reset expire time
        SeckillUser user = redisService.get(SeckillUserKey.token, token, SeckillUser.class);
        if (user != null) {
            redisService.set(SeckillUserKey.token, token, user);
            CookieUtil.writeLoginToken(response, token);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginForm loginForm) {
        if (loginForm == null) {
            log.error("【登录】参数错误");
            throw new SeckillException(ResultEnum.SERVER_ERROR);
        }
        String mobile = loginForm.getMobile();
        SeckillUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            // check mobile
            throw new SeckillException(ResultEnum.MOBILE_NOT_EXIST);
        }

        String password = loginForm.getPassword();    // fe form password
        String validPassword = user.getPassword();
        String salt = user.getSalt();
        String encryptedPass = MD5Util.formPassToDBPass(password, salt);
        if (!validPassword.equals(encryptedPass)) {
            throw new SeckillException(ResultEnum.PASSWORD_ERROR);
        }

        // distributed session
        String token = UUIDUtil.uuid();
        redisService.set(SeckillUserKey.token, token, user);
        CookieUtil.writeLoginToken(response, token);
        return true;
    }

    /**
     * Cache Aside Pattern :
     * 失效  : 先从 cache 取，没得到，则从 db 中取，成功后，放到 cache 中
     * 命中 : 从 cache 中取，取到后返回
     * 更新 : 先把数据放到 db, 成功后，让 cache 失效
     */
    public boolean updatePassword(String token, long id, String formPass) {
        SeckillUser user = getById(id);
        if (user == null) {
            throw new SeckillException(ResultEnum.MOBILE_NOT_EXIST);
        }

        // update db
        String dbPass = MD5Util.formPassToDBPass(formPass, user.getSalt());
        seckillUserMapper.updatePasswordById(dbPass, id);

        // make cache invalid
        redisService.del(SeckillUserKey.getById, "" + id);
        user.setPassword(dbPass);
        redisService.set(SeckillUserKey.token, token, user);

        return true;
    }


//    // delay session expire and put into cookie
//    private void addCookie(HttpServletResponse response, String token) {
//
//        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
//        cookie.setPath("/");
//        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
//
//
//        response.addCookie(cookie);
//    }

	/*public SeckillUser getByToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		
		return redisService.get(SeckillUserKey.token, token, SeckillUser.class);
	}
*/


}













