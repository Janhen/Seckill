package com.janhen.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.common.Const;
import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.common.redis.key.GoodsKey;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.vo.GoodsDetailVO;
import com.janhen.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods/")
@Slf4j
public class GoodsController {
	
	@Autowired
	ISeckillUserService userService;

	@Autowired
	IGoodsService iGoodsService;

	@Autowired
	RedisService redisService;

	@Autowired
	ThymeleafViewResolver viewResolver;  // manual render need  viewResolver & context
	
	@Autowired
	ApplicationContext applicationContext;
	
	@RequestMapping(value="to_list", produces="text/html;charset=utf-8")
	@ResponseBody
	public String list(Model model, SeckillUser user, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("user", user);
		
		// 1.take html file from cache
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}

		// 2.view resolver to render model to html and put into cache
		List<GoodsVO> goodsList = iGoodsService.selectSeckillGoodsVoList();
		model.addAttribute("goodsList", goodsList);
		SpringWebContext ctx = new SpringWebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
		html = viewResolver.getTemplateEngine().process("goods_list", ctx);
		
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
	}
	
	@RequestMapping(value="detail/{goodsId}")
	@ResponseBody
	public ResultVO<GoodsDetailVO> detail00(SeckillUser user, @PathVariable("goodsId") Long goodsId) {
		GoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
		
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();

		int seckillStatus = 0;
		int remainSeconds = 0;
		if (curTime < startTime) {
			seckillStatus = Const.SeckillStatusEnum.NOT_BEGIN.getCode();
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			seckillStatus = Const.SeckillStatusEnum.OVER.getCode();
			remainSeconds = -1;
		} else {
			seckillStatus = Const.SeckillStatusEnum.ON.getCode();
			remainSeconds = 0;
		}
		
		GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
		goodsDetailVO.setGoods(goods);
		goodsDetailVO.setUser(user);
		goodsDetailVO.setSeckillStatus(seckillStatus);
		goodsDetailVO.setRemainSeconds(remainSeconds);
		return ResultVO.success(goodsDetailVO);
	}
}
