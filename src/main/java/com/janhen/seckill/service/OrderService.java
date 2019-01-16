package com.janhen.seckill.service;


import java.util.Date;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.janhen.seckill.dao.OrderMapper;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.vo.GoodsVo;

@Service
@Transactional(readOnly=true)
public class OrderService {
	
	@Autowired
	OrderMapper orderMapper;

	public SeckillOrder selectSeckillOrderByUserIdAndGoodsId(Long userId, Long goodsId) {
		
		SeckillOrder order = orderMapper.selectMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
		return order;
	}
	
	@Transactional
	/** 默认每次秒杀一件 */
	/** 维护 orderInfo, maioshaorder */
	public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		// null, user.getId(), goods.getId(), 0L, goods.getGoodsName(), 1, goods.getGoodsPrice(), 1, 0, new Date(), null
		orderInfo.setUserId(user.getId());
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(1L);
		orderInfo.setGoodsCount(goods.getStockCount());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getSeckillPrice());
		orderInfo.setOrderChannel(0);
		orderInfo.setStatus(1);
		
		// SelectKey 返回注入到 orderInfor 的 id 中.
		orderMapper.insertOrderInfo(orderInfo);
		
		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setGoodsId(goods.getId());
		seckillOrder.setOrderId(orderInfo.getId());
		seckillOrder.setUserId(user.getId());
		
		orderMapper.insertSeckillOrder(seckillOrder);
		
		return orderInfo;
	}

	public OrderInfo selectOrderInfoById(Long orderId) {
		// E0
		return orderMapper.selectOrderInfoById(orderId);
	}

}
