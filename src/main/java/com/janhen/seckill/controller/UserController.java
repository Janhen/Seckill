package com.janhen.seckill.controller;

import com.janhen.seckill.exeception.SeckillException;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.redis.SeckillUserKey;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.util.CookieUtil;
import com.janhen.seckill.vo.form.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/user/")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	ISeckillUserService userService;

	@Autowired
    RedisService redisService;

	@RequestMapping(value = "to_login")
	public String toLogin() {
		return "login";
	}

	@RequestMapping(value = "do_login")
	@ResponseBody
	public ResultVO<Boolean> doLogin(HttpServletResponse response, @Valid LoginForm loginForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new SeckillException(ResultEnum.BIND_ERROR);
		}
		boolean b = userService.login(response, loginForm);
		return ResultVO.success(b);
	}

	@RequestMapping("info")
	@ResponseBody
	public ResultVO<SeckillUser> info(Model model, SeckillUser user) {
		return ResultVO.success(user);
	}

	@RequestMapping("logout")
	@ResponseBody
	public ResultVO logout(HttpServletRequest request, HttpServletResponse response) {

		redisService.del(SeckillUserKey.token, CookieUtil.readLoginToken(request));
		CookieUtil.delLoginToken(request, response);
		return ResultVO.success("SUCCESS");
	}
}
