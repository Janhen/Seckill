package com.janhen.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.GoodsKey;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.result.ResultVO;
import com.janhen.seckill.service.GoodsService;
import com.janhen.seckill.service.SeckillUserService;
import com.janhen.seckill.vo.GoodsDetailVo;
import com.janhen.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
	
	@Autowired
	SeckillUserService userService;
	
	@Autowired
    RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	// mananul render need  viewResolver & context
	@Autowired
	ThymeleafViewResolver viewResolver;
	
	@Autowired
	ApplicationContext applicationContext;
	
	/**
	 * 页面缓存 ：手动渲染返回页面, 从缓存中取
	 */
	@RequestMapping(value="/to_list", produces="text/html;charset=utf-8")
	@ResponseBody
	public String list(Model model, SeckillUser user,
			HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("user", user);
		
		// BR0.1. cache has
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		
		List<GoodsVo> goodsList = goodsService.selectGoodsVoList();
		model.addAttribute("goodsList", goodsList);
		
		// Core. manaul resolve model to view
		SpringWebContext ctx = new SpringWebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
		html = viewResolver.getTemplateEngine().process("goods_list", ctx);
		
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		
		return html;
	}
	
	/**
	 * 页面静态化,前后端分离 ：异步获取填充
	 */
	@RequestMapping(value="/detail/{goodsId}")
	@ResponseBody
	public ResultVO<GoodsDetailVo> detail00(SeckillUser user,
                                            @PathVariable("goodsId") Long goodsId) {

		log.info("param:{}", goodsId);
		GoodsVo goods = goodsService.selectGoodsVoByGoodsId(goodsId);
		
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();
		
		int seckillStatus = 0;
		int remainSeconds = 0;
		
		if (curTime < startTime) {
			seckillStatus = 0;
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			seckillStatus = 2;
			remainSeconds = -1;
		} else {
			seckillStatus = 1;
			remainSeconds = 0;
		}
		
		GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
		goodsDetailVo.setGoods(goods);
		goodsDetailVo.setUser(user);
		goodsDetailVo.setSeckillStatus(seckillStatus);
		goodsDetailVo.setRemainSeconds(remainSeconds);
		
		return ResultVO.success(goodsDetailVo);
	}
	
	
	
	
	
	
	
	
	//  depred
	
	/**
	 * Ver0. 页面缓存 ：手动渲染页面，放入缓存中
	 *//*
	@RequestMapping(value="/to_detail0/{goodsId}", produces="text/html")
	@ResponseBody
	public String detail0(Model model, SeckillUser user,
			@PathVariable("goodsId") Long goodsId,
			HttpServletRequest request, HttpServletResponse response) {
		// BR1. cache aleady have
		String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		
		GoodsVo goods = goodsService.selectGoodsVoByGoodsId(goodsId);
		model.addAttribute("user", user);
		model.addAttribute("goods", goods);
		
		int seckillStatus = 0;
		int remainSeconds = 0;
		
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();
		
		if (curTime < startTime) {
			// br1. seckill is not start
			seckillStatus = 0;
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			// br2. seckill is over
			seckillStatus = 2;
			remainSeconds = -1;
		} else {
			seckillStatus = 1;
			remainSeconds = 0;
		}
		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		
		
		// Core. manaual resolve
		SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), 
				request.getLocale(), model.asMap(), applicationContext);
		html = viewResolver.getTemplateEngine().process("goods_detail", ctx);
		
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
			
		}
		return html;
	}*/
	
	
	
	
	
	
	/**
	 * Ver1. 返回页面并填充数据
	 */
	/*@RequestMapping(value = "/to_list2")
	public String list2(Model model, 
			SeckillUser user) {

		List<GoodsVo> goodsList = goodsService.selectGoodsVoList();
		
		model.addAttribute("user", user);
		model.addAttribute("goodsList", goodsList);
		return "goods_list";
	}*/
	
	
	/**
	 * Ver2. 返回页面并填充数据
	 */
	/*@RequestMapping(value="/to_detail2/{goodsId}")
	public String toDetail(Model model, SeckillUser user,
			@PathVariable("goodsId") Long goodsId) {
		int seckillStatus = 0;
		int remainSeconds = 0;
		
		GoodsVo goods = goodsService.selectGoodsVoByGoodsId(goodsId);
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();
		
		if (curTime < startTime) {
			// BR1. seckill not start
			seckillStatus = 0;
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			// BR2. sekill over
			seckillStatus = 2;
			remainSeconds = -1;
		} else {
			// BR. seckill running
			seckillStatus = 1;
			remainSeconds = 0;
		}
		
		model.addAttribute("user", user);
		model.addAttribute("goods", goods);
		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		
		return "goods_detail";
	}*/
	
	
	
	
	
	
	/*@CookieValue(value=SeckillUserService.COOKIE_NAME_TOKEN, required=false) String cookieToken,
		@RequestParam(value=SeckillUserService.COOKIE_NAME_TOKEN, required=false) String paramToken
		// E0
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) { return "login"; }
		
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		SeckillUser user = userService.getByToken(token);
		
		model.addAttribute("user", user);
		return "goods_list";*/

}
