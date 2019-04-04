package com.janhen.seckill.controller;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.common.rabbitmq.MQSender;
import com.janhen.seckill.common.rabbitmq.SeckillMessage;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.BasePrefix;
import com.janhen.seckill.common.redis.key.GoodsKey;
import com.janhen.seckill.controller.interceptor.AccessLimit;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.IOrderService;
import com.janhen.seckill.service.ISeckillService;
import com.janhen.seckill.service.ISeckillUserService;
import com.janhen.seckill.vo.SeckillGoodsVO;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill/")
@Slf4j
public class SeckillController implements InitializingBean {
	
	@Autowired
	ISeckillUserService iSeckillUserService;
	
	@Autowired
	IGoodsService iGoodsService;
	
	@Autowired
	IOrderService iOrderService;
	
	@Autowired
	ISeckillService iSeckillService;

	@Autowired
	RedisService redisService;

	@Autowired
    MQSender sender;

	// modify concurrentHashMap to hashMap AND not have put operation AND value only modify one time AND init is single thread execute
	private Map<Long, Boolean> localOverMap = new HashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// preload cache and init map
		List<SeckillGoodsVO> goodsList = iGoodsService.selectSeckillGoodsVoList();          // only store seckill good
		if (goodsList != null) {
			for (SeckillGoodsVO goods : goodsList) {
				redisService.set(GoodsKey.getSeckillGoodsStockByGid, BasePrefix.getKey(goods.getId()), goods.getStockCount());
				localOverMap.put(goods.getId(), goods.getStockCount() <= 0);
			}
		}
	}

	@RequestMapping(value="verifyCode")
	@ResponseBody
	public ResultVO<String> getVerfiyCode(HttpServletResponse response, SeckillUser user, @RequestParam("goodsId") Long goodsId) {
		BufferedImage image = iSeckillService.generateVerfiyCodeImg(user, goodsId);
		try (ServletOutputStream out = response.getOutputStream()) {
			// response 写回
			ImageIO.write(image, "JPEG", out);
			out.flush();
			return null;
		} catch (IOException e) {
			log.error("【获取秒杀验证码】失败", e);
			return ResultVO.error(ResultEnum.SECKILL_FAIL);
		}
	}

	@RequestMapping(value="path", method=RequestMethod.GET)
	@ResponseBody
	@AccessLimit(seconds=10, maxCount=2)
	public ResultVO<String> getSeckillPath(HttpServletRequest request, SeckillUser user,
                                           @RequestParam("goodsId") Long goodsId,
                                           @RequestParam("verifyCode") Integer verfiyCode) {
		boolean isValid = iSeckillService.checkVerifyCode(user, goodsId, verfiyCode);
		if (!isValid) {
			log.error("【获取秒杀路径】验证码不正确");
			return ResultVO.error(ResultEnum.REQUEST_ILLEGAL);
		}
		String seckillPath = iSeckillService.generateSeckillPath(user, goodsId);
		return ResultVO.success(seckillPath);
	}
	
	@RequestMapping(value={"{path}/do_seckill"}, method={RequestMethod.POST})
	@ResponseBody
	@AccessLimit
	public ResultVO<Integer> seckill(Model model, SeckillUser user,
									  @PathVariable("path") String path, @RequestParam("goodsId") Long goodsId) {
		model.addAttribute("user", user);

		// 1.check seckill path from redis cache
		boolean isCorrect = iSeckillService.checkPath(user, goodsId, path);
		if (!isCorrect) {
			log.error("【秒杀】路径非法");
			return ResultVO.error(ResultEnum.REQUEST_ILLEGAL);
		}

		// 2.judge memory tag that stock whether or not empty
		boolean isOver = localOverMap.get(goodsId);
		if (isOver) {
			log.error("【秒杀】秒杀结束");
			return ResultVO.error(ResultEnum.SECKILL_OVER);
		}
		
		// 3.decrease stock from redis cache AND have additional function that judge seckill goods is over AND not consistent with DB stock
		Long stock = redisService.decr(GoodsKey.getSeckillGoodsStockByGid, BasePrefix.getKey(goodsId));
		if (stock < 0) {
			localOverMap.put(goodsId, true);
			log.error("【秒杀】秒杀结束");
			return ResultVO.error(ResultEnum.SECKILL_OVER);
		}
		
		// 4. Business strategy: check user already seckill from cache OR DB
		SeckillOrder order = iOrderService.selectSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {
			log.error("【秒杀】秒杀重复,userId:{},goodsId{}", user.getId(), goodsId);
			return ResultVO.error(ResultEnum.SECKILL_REPEATE);
		}

		// 5.put into message queue
		SeckillMessage message = new SeckillMessage(user, goodsId);
		sender.sendSeckillMessage(message);
		return ResultVO.success(0);
	}

	@RequestMapping(value="result")
	@ResponseBody
	public ResultVO<Long> result(SeckillUser user, @RequestParam("goodsId") Long goodsId ) {
		if (user == null) {
			return ResultVO.error(ResultEnum.SESSION_ERROR);
		}
		log.info("User{}", user);
		Long orderId = iSeckillService.getSeckillResult(user.getId(), goodsId);
		return ResultVO.success(orderId);
	}
}
