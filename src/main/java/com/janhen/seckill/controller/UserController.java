package com.janhen.seckill.controller;

import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.result.ResultVO;
import com.janhen.seckill.service.SeckillUserService;
import com.janhen.seckill.vo.form.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	SeckillUserService userService;

	@Autowired
    RedisService redisService;

	@RequestMapping(value = "/to_login")
	public String toLogin() {
		return "login";
	}

	@RequestMapping(value = "/do_login")
	@ResponseBody
	public ResultVO<Boolean> doLogin(HttpServletResponse response, @Valid LoginForm loginForm) {
		log.info(loginForm.toString());

		boolean b = userService.login(response, loginForm);
		return ResultVO.success(b);
	}

	@RequestMapping("/info")
	@ResponseBody
	public ResultVO<SeckillUser> info(Model model, SeckillUser user) {
		return ResultVO.success(user);
	}
}
