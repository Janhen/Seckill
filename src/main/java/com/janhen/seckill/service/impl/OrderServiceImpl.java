package com.janhen.seckill.service.impl;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.exeception.SeckillException;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.common.redis.key.BasePrefix;
import com.janhen.seckill.common.redis.key.OrderKey;
import com.janhen.seckill.dao.OrderMapper;
import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.IOrderService;
import com.janhen.seckill.vo.SeckillGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly=true)
@Slf4j
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	OrderMapper orderMapper;

	@Autowired
	RedisService redisService;

	public SeckillOrder selectSeckillOrderByUserIdAndGoodsId(Long userId, Long goodsId) {
		SeckillOrder seckillOrder = redisService.get(OrderKey.getSeckillOrderByUidGid, BasePrefix.getKey(userId, goodsId), SeckillOrder.class);
		if (seckillOrder == null) {
			seckillOrder = orderMapper.selectSeckillOrderByUserIdAndGoodsId(userId, goodsId);
		}
		return seckillOrder;
	}

	/**
	 * unique_index(user_id, goods_id)
	 * @param user
	 * @param goods
	 * @return
	 */
	@Transactional
	public OrderInfo createOrder(SeckillUser user, SeckillGoodsVO goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setUserId(user.getId());
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(1L);
		orderInfo.setGoodsCount(goods.getStockCount());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getSeckillPrice());
		orderInfo.setOrderChannel(0);
		orderInfo.setStatus(1);
		// SelectKey 返回注入到 orderInfo 的 id 中.
		orderMapper.insertOrderInfo(orderInfo);

		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setGoodsId(goods.getId());
		// have orderId, use mybatis to injected
		seckillOrder.setOrderId(orderInfo.getId());
		seckillOrder.setUserId(user.getId());
		// control to prevent oversold
		orderMapper.insertSeckillOrder(seckillOrder);
		// put into redis to prevent one time read from DB in seckill AND query
		redisService.set(OrderKey.getSeckillOrderByUidGid, BasePrefix.getKey(user.getId(), goods.getId()), seckillOrder);
		return orderInfo;
	}

	public OrderInfo selectOrderInfoById(Long orderId) {
		OrderInfo orderInfo = orderMapper.selectOrderInfoById(orderId);
		if (orderInfo == null) {
			log.error("【查询订单】订单不存在, orderId:{}", orderId);
			throw new SeckillException(ResultEnum.ORDER_NOT_EXIST);
		}
		return orderInfo;
	}
}
