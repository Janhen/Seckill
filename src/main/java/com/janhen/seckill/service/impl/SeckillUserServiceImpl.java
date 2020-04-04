package com.janhen.seckill.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.exeception.SeckillException;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.BasePrefix;
import com.janhen.seckill.common.redis.key.SeckillUserKey;
import com.janhen.seckill.dao.SeckillUserMapper;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.util.CookieUtil;
import com.janhen.seckill.util.KeyUtil;
import com.janhen.seckill.util.MD5Util;
import com.janhen.seckill.vo.form.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class SeckillUserServiceImpl implements ISeckillUserService {

  @Autowired
  private SeckillUserMapper seckillUserMapper;

  @Autowired
  private RedisService redisService;

  public SeckillUser getById(long id) {
    SeckillUser seckillUser = redisService.get(SeckillUserKey.getById, "" + id, SeckillUser.class);
    if (seckillUser != null) {
      return seckillUser;
    }
    seckillUser = seckillUserMapper.getById(id);
    if (seckillUser != null) {
      redisService.set(SeckillUserKey.getById, "" + id, seckillUser);
    }
    return seckillUser;
  }

  public SeckillUser getByToken(HttpServletResponse response, String token) {
    if (StringUtils.isEmpty(token)) {
      return null;
    }
    // take from cache AND reset expire time(cache, cookie)
    SeckillUser user = redisService.get(SeckillUserKey.token, token, SeckillUser.class);
    if (user != null) {
      redisService.set(SeckillUserKey.token, token, user);    // reset session expire time
      CookieUtil.writeLoginToken(response, token);             // reset cookie expire time
    }
    return user;
  }

  public boolean login(HttpServletResponse response, LoginForm loginForm) {
    // loginForm must valid by JSR303 ensure
//        if (loginForm == null) {
//            log.error("【登录】参数错误");
//            throw new SeckillException(ResultEnum.SERVER_ERROR);
//        }
    String mobile = loginForm.getMobile();
    SeckillUser user = getById(Long.parseLong(mobile));
    if (user == null) {
      throw new SeckillException(ResultEnum.MOBILE_NOT_EXIST);
    }

    String password = loginForm.getPassword();    // fe form password
    String validPassword = user.getPassword();
    String salt = user.getSalt();
    String encryptedPass = MD5Util.formPassToDBPass(password, salt);
    if (!validPassword.equals(encryptedPass)) {
      throw new SeckillException(ResultEnum.PASSWORD_ERROR);
    }

    String token = KeyUtil.geneToken();
    redisService.set(SeckillUserKey.token, token, user);
    CookieUtil.writeLoginToken(response, token);
    return true;
  }

  public String login2(HttpServletResponse response, LoginForm loginForm) {
    // loginForm must valid by JSR303 ensure
//        if (loginForm == null) {
//            log.error("【登录】参数错误");
//            throw new SeckillException(ResultEnum.SERVER_ERROR);
//        }
    String mobile = loginForm.getMobile();
    SeckillUser user = getById(Long.parseLong(mobile));
    if (user == null) {
      throw new SeckillException(ResultEnum.MOBILE_NOT_EXIST);
    }

    String password = loginForm.getPassword();    // fe form password
    String validPassword = user.getPassword();
    String salt = user.getSalt();
    String encryptedPass = MD5Util.formPassToDBPass(password, salt);
    if (!validPassword.equals(encryptedPass)) {
      throw new SeckillException(ResultEnum.PASSWORD_ERROR);
    }

    String token = KeyUtil.geneToken();
    redisService.set(SeckillUserKey.token, token, user);
    CookieUtil.writeLoginToken(response, token);
    return token;
  }

  @Override
  public boolean updatePassword(String token, long id, String formPass) {
    SeckillUser user = getById(id);
    if (user == null) {
      throw new SeckillException(ResultEnum.MOBILE_NOT_EXIST);
    }

    // update db
    String dbPass = MD5Util.formPassToDBPass(formPass, user.getSalt());
    seckillUserMapper.updatePasswordById(dbPass, id);

    // make cache invalid
    redisService.del(SeckillUserKey.getById, BasePrefix.getKey(id));
    user.setPassword(dbPass);
    redisService.set(SeckillUserKey.token, token, user);
    return true;
  }
}













