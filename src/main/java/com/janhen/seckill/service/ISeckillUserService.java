package com.janhen.seckill.service;

import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.vo.form.LoginForm;

import javax.servlet.http.HttpServletResponse;

public interface ISeckillUserService {

  SeckillUser getById(long id);

  SeckillUser getByToken(HttpServletResponse response, String token);

  boolean login(HttpServletResponse response, LoginForm loginForm);

  /**
   * user for generating user token
   *
   * @param response
   * @param loginForm
   * @return
   */
  String login2(HttpServletResponse response, LoginForm loginForm);

  boolean updatePassword(String token, long id, String formPass);
}
