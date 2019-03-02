package com.janhen.seckill.controller;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.pojo.User;
import com.janhen.seckill.common.rabbitmq.MQSender;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.UserKey;
import com.janhen.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/")
public class TestController {
	
	@Autowired
	UserService userService;

	@Autowired
	RedisService redisService;
	
	@Autowired
    MQSender sender;
	
	@RequestMapping("hello")
	@ResponseBody
	public ResultVO<String> home() {
		return ResultVO.success("Hello World!");
	}
	
	@RequestMapping("error")
    @ResponseBody
    public ResultVO<String> error() {
        return ResultVO.error(ResultEnum.SESSION_ERROR);
    }
    
    @RequestMapping("hello/themaleaf")
    public String themaleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }
	
	@RequestMapping("db/get")
	@ResponseBody
	public ResultVO<User> doGet(Model model) {
		User user = userService.getById(2);
		return ResultVO.success(user);
	}
	
	@RequestMapping("db/tx")
    @ResponseBody
    public ResultVO<Boolean> dbTx() {
    	userService.tx();
        return ResultVO.success(true);
    }
	
	@RequestMapping("redis/get")
    @ResponseBody
    public ResultVO<User> redisGet() {
    	User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        return ResultVO.success(user);
    }
	
	@RequestMapping("redis/set")
    @ResponseBody
    public ResultVO<Boolean> redisSet() {
    	User user  = new User(1, "1111");
    	redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return ResultVO.success(true);
    }
	
	@RequestMapping(value="mq")
	@ResponseBody
	public ResultVO<String> mq() {
		SeckillUser user = new SeckillUser();
		user.setId(2262L);
		user.setNickname("jack");
		user.setPassword("fsdfewrgsfsdf");
		
		sender.send(user);
		return ResultVO.success("hello world!");
	}
}
