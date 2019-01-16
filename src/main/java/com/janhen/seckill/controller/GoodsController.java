package com.janhen.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.common.SeckillStatusEnum;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.GoodsKey;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.vo.GoodsDetailVO;
import com.janhen.seckill.vo.GoodsVO;
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
	
	/**
	 * 页面缓存：手动渲染返回页面, 从缓存中取
	 */
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
		List<GoodsVO> goodsList = iGoodsService.selectGoodsVoList();
		model.addAttribute("goodsList", goodsList);
		SpringWebContext ctx = new SpringWebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
		html = viewResolver.getTemplateEngine().process("goods_list", ctx);
		
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
	}
	
	/**
	 * 页面静态化,前后端分离 ：异步获取填充
	 */
	@RequestMapping(value="detail/{goodsId}")
	@ResponseBody
	public ResultVO<GoodsDetailVO> detail00(SeckillUser user, @PathVariable("goodsId") Long goodsId) {

		log.info("param:{}", goodsId);
		GoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
		
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();

		int seckillStatus = 0;
		int remainSeconds = 0;
		if (curTime < startTime) {
			seckillStatus = SeckillStatusEnum.NOT_BEGIN.getCode();
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			seckillStatus = SeckillStatusEnum.OVER.getCode();
			remainSeconds = -1;
		} else {
			seckillStatus = SeckillStatusEnum.ON.getCode();
			remainSeconds = 0;
		}
		
		GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
		goodsDetailVO.setGoods(goods);
		goodsDetailVO.setUser(user);
		goodsDetailVO.setSeckillStatus(seckillStatus);
		goodsDetailVO.setRemainSeconds(remainSeconds);
		return ResultVO.success(goodsDetailVO);
	}
	
	
	
	/**
	 * 页面缓存：手动渲染页面，放入缓存中
	 */
	@RequestMapping(value="to_detail_page/{goodsId}", produces="text/html")
	@ResponseBody
	public String detailPage(Model model, SeckillUser user,
			@PathVariable("goodsId") Long goodsId,
			HttpServletRequest request, HttpServletResponse response) {
		// 1.take from cache
		String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}

		// 2. take from db and render data to html, put html file into cache
		GoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
		model.addAttribute("user", user);
		model.addAttribute("goods", goods);
		
		int seckillStatus = 0;
		int remainSeconds = 0;
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();
		if (curTime < startTime) {
			seckillStatus = SeckillStatusEnum.NOT_BEGIN.getCode();
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			seckillStatus = SeckillStatusEnum.OVER.getCode();
			remainSeconds = -1;
		} else {
			seckillStatus = SeckillStatusEnum.ON.getCode();
			remainSeconds = 0;
		}
		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		
		// Core. resolve manually
		SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), 
				request.getLocale(), model.asMap(), applicationContext);
		html = viewResolver.getTemplateEngine().process("goods_detail", ctx);
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
		}
		return html;
	}
	
	
	/**
	 * 原始商品列表
	 */
	@RequestMapping(value = "to_list_origin")
	public String list2(Model model, SeckillUser user) {
		List<GoodsVO> goodsList = iGoodsService.selectGoodsVoList();
		model.addAttribute("user", user);
		model.addAttribute("goodsList", goodsList);
		return "goods_list";
	}
	
	
	/**
	 * 原始商品详情
	 */
	@RequestMapping(value="to_detail_origin/{goodsId}")
	public String toDetail(Model model, SeckillUser user, @PathVariable("goodsId") Long goodsId) {

		GoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
		int seckillStatus = 0;
		int remainSeconds = 0;
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long curTime = System.currentTimeMillis();
		
		if (curTime < startTime) {
			seckillStatus = SeckillStatusEnum.NOT_BEGIN.getCode();
			remainSeconds = (int) ((startTime - curTime) / 1000);
		} else if (curTime > endTime) {
			seckillStatus = SeckillStatusEnum.OVER.getCode();
			remainSeconds = -1;
		} else {
			seckillStatus = SeckillStatusEnum.ON.getCode();
			remainSeconds = 0;
		}
		model.addAttribute("user", user);
		model.addAttribute("goods", goods);
		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		return "goods_detail";
	}

//	public String ddd(@CookieValue(value=SeckillUserService.COOKIE_NAME_TOKEN, required=false) String cookieToken,
//		@RequestParam(value=SeckillUserService.COOKIE_NAME_TOKEN, required=false) String paramToken, Model model) {
//		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//			return "login";
//		}
//
//		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//		SeckillUser user = iSeckillUserService.getByToken(token);
//
//		model.addAttribute("user", user);
//		return "goods_list";
//	}
}
