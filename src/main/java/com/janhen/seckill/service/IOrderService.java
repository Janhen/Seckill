package com.janhen.seckill.service;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.vo.GoodsVO;

public interface IOrderService {

    SeckillOrder selectSeckillOrderByUserIdAndGoodsId(Long userId, Long goodsId);

    OrderInfo createOrder(SeckillUser user, GoodsVO goods);

    OrderInfo selectOrderInfoById(Long orderId);
}
