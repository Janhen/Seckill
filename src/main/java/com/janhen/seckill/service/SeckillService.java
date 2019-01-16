package com.janhen.seckill.service;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.redis.SeckillKey;
import com.janhen.seckill.util.MD5Util;
import com.janhen.seckill.util.UUIDUtil;
import com.janhen.seckill.vo.GoodsVo;
import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.redis.SeckillKey;
import com.janhen.seckill.util.MD5Util;
import com.janhen.seckill.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
@Transactional(readOnly=true)
public class SeckillService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
    RedisService redisService;

	@Transactional
	public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
		// desc stock by goods.id
		boolean isSuccess = goodsService.descStock(goods);
		
		if (isSuccess) {
			// create orderinfo by user, goodsVo
			OrderInfo orderInfo = orderService.createOrder(user, goods);
			
			return orderInfo;
		} else {
			setGoodsOver(goods.getId());
			return null;
		}
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
	}

	/** #. path */
	public String generateSeckillPath(SeckillUser user, Long goodsId) {
		String seckillPath = MD5Util.md5(UUIDUtil.uuid()) + System.currentTimeMillis() % 1000;
		redisService.set(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, seckillPath);
		return seckillPath;
	}
	
	public boolean checkPath(SeckillUser user, Long goodsId, String path) {
		if (user == null || goodsId == null || StringUtils.isEmpty(path)) {
			return false;
		}
		
		String cachedPath = redisService.get(SeckillKey.getSeckillPath, String.valueOf(user.getId() + "_" + goodsId), String.class);
		
		return path.equals(cachedPath);
	}

	/** #. verfiycode */
	public BufferedImage generateVerfiyCodeImg(SeckillUser user, Long goodsId) {
		if (user == null || goodsId == null) 
			return null;
		
		// create the image in memory
		int width = 80, height = 32;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		
		// Core. generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		
		// 把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, rnd);
		
		return image;
	}
	
	/**
	 * + - * 
	 */
	private static char[] ops = new char[] {'+', '-', '*'};
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
	    int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = ""+ num1 + op1 + num2 + op2 + num3;
		return exp;
	}
	
	/**
	 * ScriptEngine
	 */
	private static int calc(String exp) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			return (Integer) engine.eval(exp);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	

	public boolean checkVerifyCode(SeckillUser user, Long goodsId, Integer verfiyCode) {
		if (user == null || goodsId == null || verfiyCode == null) 
			return false;
		
		Integer cachedCode = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId, Integer.class);
		if (cachedCode == null || cachedCode - verfiyCode != 0) {
			return false;
		}
		
		// delete cache
		redisService.del(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId);
		
		return true;
	}
	
	

	public Long getSeckillResult(Long userId, Long goodsId) {
		if (goodsId == null) {
			return -1L;
		}
		
		SeckillOrder order = orderService.selectSeckillOrderByUserIdAndGoodsId(userId, goodsId);
		if (order != null) {
			// BR1. success
			return order.getId();
		} else if (getGoodsOver(goodsId)) {
			
			// BR2. is over
			return -1L;
			
		} else {
			// BR. wait on queue
			return 0L;
		}
	}
	
	
	
	private boolean getGoodsOver(Long goodsId) {
		return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
	}

	public static void main(String[] args) {
		System.out.println(calc("44 + 56 + 2 * 7"));
	}

}
