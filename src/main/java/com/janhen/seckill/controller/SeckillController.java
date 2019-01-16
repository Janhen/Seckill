package com.janhen.seckill.controller;

import com.janhen.seckill.interceptor.AccessLimit;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.rabbitmq.MQSender;
import com.janhen.seckill.rabbitmq.SeckillMessage;
import com.janhen.seckill.redis.AccessKey;
import com.janhen.seckill.redis.GoodsKey;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.result.CodeMsg;
import com.janhen.seckill.result.ResultVO;
import com.janhen.seckill.service.GoodsService;
import com.janhen.seckill.service.OrderService;
import com.janhen.seckill.service.SeckillService;
import com.janhen.seckill.service.SeckillUserService;
import com.janhen.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean{
	
	@Autowired
	SeckillUserService userService;
	
	@Autowired
    RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired 
	OrderService orderService;
	
	@Autowired
	SeckillService seckillService;

	@Autowired
    MQSender sender;

	private Map<Long, Boolean> localOverMap = new ConcurrentHashMap<>();
	
	// put the goods stock into cache and init localOverMap
	@Override
	public void afterPropertiesSet() throws Exception {
		// ★★ 缓存预加载
		List<GoodsVo> goodsList = goodsService.selectGoodsVoList();
		if (goodsList != null) {
			for (GoodsVo goods : goodsList) {
				redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), goods.getStockCount());
				localOverMap.put(goods.getId(), false);
			}
		}
		return ;
	}
	

	@RequestMapping(value="/path", method=RequestMethod.GET)
	@ResponseBody
	@AccessLimit(seconds=5, maxCount=5, needLogin=true)
	public ResultVO<String> getSeckillPath(HttpServletRequest request, SeckillUser user,
                                           @RequestParam("goodsId") Long goodsId,
                                           @RequestParam("verifyCode") Integer verfiyCode) {
		/*if (user == null) {
			return ResultVO.error(CodeMsg.SESSION_ERROR);
		}*/
		
		// prevent interface brush
		String key = request.getRequestURI() + "_" + user.getId();
		Integer accessCount = redisService.get(AccessKey.access, key, Integer.class);
		if (accessCount == null) {
			redisService.set(AccessKey.access, key, 1);
		} else if (accessCount < 5) {
			redisService.incr(AccessKey.access, key);
		} else {
			return ResultVO.error(CodeMsg.ACCESS_LIMIT_REACHED);
		}

		// check the verify code
		boolean isValid = seckillService.checkVerifyCode(user, goodsId, verfiyCode);
		if (!isValid) {
			return ResultVO.error(CodeMsg.REQUEST_ILLEGAL);
		}
		String seckillPath = seckillService.generateSeckillPath(user, goodsId);
		return ResultVO.success(seckillPath);
	}
	
	@RequestMapping(value="/{path}/do_seckill", method=RequestMethod.POST)
	@ResponseBody
	public ResultVO<Integer> seckill0(Model model, SeckillUser user,
									  @PathVariable("path") String path,
									  @RequestParam("goodsId") Long goodsId) {
		model.addAttribute("user", user);
		if (user == null) { return ResultVO.error(CodeMsg.SESSION_ERROR); }
		
		// check path from redis cache
		boolean isValid = seckillService.checkPath(user, goodsId, path);
		if (!isValid) {
			return ResultVO.error(CodeMsg.REQUEST_ILLEGAL);
		}
		
		// memory 
		boolean over = localOverMap.get(goodsId);
		if (over) {
			return ResultVO.error(CodeMsg.SECKILL_OVER);
		}
		
		// decr from redis cache
		Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
		if (stock < 0) {
			localOverMap.put(goodsId, true);
			return ResultVO.error(CodeMsg.SECKILL_OVER);
		}
		
		// check user already have seckill
		SeckillOrder order = orderService.selectSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {
			return ResultVO.error(CodeMsg.SECKILL_REPEATE);
		}
		
		// put into queue 
		SeckillMessage message = new SeckillMessage(user, goodsId);
		sender.send(message);
		
		return ResultVO.success(0);
		
		
		
		
		/*// Core 
		// BR1. stock is empty
		GoodsVo goods = goodsService.selectGoodsVoByGoodsId(goodsId);
		if (goods.getStockCount() <= 0) {
			return ResultVO.error(CodeMsg.SECKILL_FAIL);
		}
		
		// BR2. user already have seckill
		SeckillOrder seckillOrder = orderService.selectSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (seckillOrder != null) {
			return ResultVO.error(CodeMsg.SECKILL_REPEATE);
		}
		
		// BR
		OrderInfo orderInfo = seckillService.seckill(user, goods);
		
		return ResultVO.success(orderInfo);*/
	}
	
	@RequestMapping(value="/verifyCode")
	@ResponseBody
	public ResultVO<String> getVerfiyCode(HttpServletResponse response, SeckillUser user,
										  @RequestParam("goodsId") Long goodsId) {
		/*if (user == null) {
			return ResultVO.error(CodeMsg.SESSION_ERROR);
		}*/
		
		BufferedImage image = seckillService.generateVerfiyCodeImg(user, goodsId);
		
		try (ServletOutputStream out = response.getOutputStream()) {
			
			ImageIO.write(image, "JPEG", out);
			out.flush();
			
			// ajax 风格调用
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return ResultVO.error(CodeMsg.SECKILL_FAIL);
		} 
	}

	@RequestMapping(value="/result")
	@ResponseBody
	public ResultVO<Long> result(SeckillUser user,
								 @RequestParam("goodsId") Long goodsId ) {
		if (user == null) {
			return ResultVO.error(CodeMsg.SESSION_ERROR);
		}
		
		Long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
		
		return ResultVO.success(orderId);
	}
	
	
	
	
	
	/*// raw access
	@RequestMapping(value="/do_seckill1")
	public String seckill(Model model,SeckillUser user,
			@RequestParam("goodsId") Long goodsId) {
		if (user == null) { return "login"; }
		
		GoodsVo goods = goodsService.selectGoodsVoByGoodsId(goodsId);
		// BR1. stock is empty
		if (goods.getStockCount() <= 0) {
			model.addAttribute("errormsg", CodeMsg.SECKILL_OVER.getMsg());
			return "seckill_fail";
		}
		
		SeckillOrder order = orderService.selectSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		// BR2. user have a seckill then seckill fail
		if (order != null) {
			model.addAttribute("errormsg", CodeMsg.SECKILL_REPEATE.getMsg());
			return "seckill_fail";
		}
		
		// BR desc stock and insert seckillorder
		OrderInfo orderInfo = seckillService.seckill(user, goods);
		
		model.addAttribute("user", user);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("goods", goods);
		
		return "order_detail";
	}*/

}
