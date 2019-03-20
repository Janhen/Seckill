package com.janhen.seckill.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.janhen.seckill.common.Const;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.BasePrefix;
import com.janhen.seckill.common.redis.key.SeckillKey;
import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.IOrderService;
import com.janhen.seckill.service.ISeckillService;
import com.janhen.seckill.util.KeyUtil;
import com.janhen.seckill.vo.SeckillGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
@Transactional(readOnly=true)
public class SeckillServiceImpl implements ISeckillService {
	
	@Autowired
	IGoodsService iGoodsService;
	
	@Autowired
	IOrderService iOrderService;
	
	@Autowired
    RedisService redisService;

	@Transactional
	public OrderInfo seckill(SeckillUser user, SeckillGoodsVO goods) {
		// execute only by message queue
		boolean isSuccess = iGoodsService.descStock(goods);
		if (isSuccess) {
			// create orderinfo by user, goodsVo; may error for unique index
			OrderInfo orderInfo = iOrderService.createOrder(user, goods);
			return orderInfo;
		} else {
			setGoodsOver(goods.getId());
			return null;
		}
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(SeckillKey.isGoodsOverByGid, BasePrefix.getKey(goodsId), true);
	}

	public String generateSeckillPath(SeckillUser user, Long goodsId) {
		String seckillPath = KeyUtil.geneSeckillPath();
		redisService.set(SeckillKey.getSeckillPathByUidGid, BasePrefix.getKey(user.getId(), goodsId), seckillPath);
		return seckillPath;
	}
	
	public boolean checkPath(SeckillUser user, Long goodsId, String path) {
		if (user == null || goodsId == null || StringUtils.isEmpty(path)) {
			return false;
		}
		// String key = user.getId() + Const.SPLIT + goodsId;
		String cachedPath = redisService.get(SeckillKey.getSeckillPathByUidGid, BasePrefix.getKey(user.getId(), goodsId), String.class);
		return path.equals(cachedPath);
	}

	public BufferedImage generateVerfiyCodeImg(SeckillUser user, Long goodsId) {
		if (user == null) {
			return null;
		}

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
		String verifyCode = KeyUtil.geneVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();

		// Put verify code result into redis
		int calcResult = KeyUtil.calc(verifyCode);
		redisService.set(SeckillKey.getSeckillVerifyCodeResultByUidGid, BasePrefix.getKey(user.getId(), goodsId), calcResult);
		return image;
	}
	

	public boolean checkVerifyCode(SeckillUser user, Long goodsId, Integer verfiyCode) {
		if (user == null || goodsId == null || verfiyCode == null) {
			return false;
		}
		Integer cachedCode = redisService.get(SeckillKey.getSeckillVerifyCodeResultByUidGid, BasePrefix.getKey(user.getId(), goodsId), Integer.class);
		if (cachedCode == null || cachedCode != verfiyCode) {
			return false;
		}
		// delete cache when check success
		redisService.del(SeckillKey.getSeckillVerifyCodeResultByUidGid, BasePrefix.getKey(user.getId(), goodsId));
		return true;
	}

	public Long getSeckillResult(Long userId, Long goodsId) {
		if (goodsId == null) {
			return -1L;
		}
		SeckillOrder order = iOrderService.selectSeckillOrderByUserIdAndGoodsId(userId, goodsId);
		if (order != null) {
			return order.getOrderId();
		} else if (checkGoodsIsOver(goodsId)) {
			return Const.SeckillOrderStatusEnum.OVER.getCode();
		} else {
			return Const.SeckillOrderStatusEnum.WAIT_ON_QUEUE.getCode();
		}
	}
	
	private boolean checkGoodsIsOver(Long goodsId) {
		return redisService.exists(SeckillKey.isGoodsOverByGid, BasePrefix.getKey(goodsId));
	}
}
