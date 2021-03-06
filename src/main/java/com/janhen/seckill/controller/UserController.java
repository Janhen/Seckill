package com.janhen.seckill.controller;

import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.SeckillUserKey;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.util.CookieUtil;
import com.janhen.seckill.vo.form.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
public class UserController {

	@Autowired
	ISeckillUserService userService;

	@Autowired
    RedisService redisService;

	@RequestMapping(value = {"/user/to_login", "/", "/index"})
	public String toLogin() {
		return "login";
	}

	@RequestMapping(value = "/user/do_login")
	@ResponseBody
	public ResultVO<Boolean> doLogin(HttpServletResponse response, @Valid LoginForm loginForm) {  // , BindingResult bindingResult
		boolean b = userService.login(response, loginForm);
		return ResultVO.success(b);
	}

	@RequestMapping(value = "/user/do_login2")
	@ResponseBody
	public ResultVO<String> doLogin2(HttpServletResponse response, String mobile, String password) {  // , BindingResult bindingResult
		String token = userService.login2(response, new LoginForm(mobile, password));
		return ResultVO.success(token);
	}

	@RequestMapping("/user/info")
	@ResponseBody
	public ResultVO<SeckillUser> info(Model model, SeckillUser user) {
		return ResultVO.success(user);
	}

	@RequestMapping("/user/logout")
	@ResponseBody
	public ResultVO logout(HttpServletRequest request, HttpServletResponse response) {
		redisService.del(SeckillUserKey.token, CookieUtil.readLoginToken(request));
		CookieUtil.delLoginToken(request, response);
		return ResultVO.success("SUCCESS");
	}
}
