package com.janhen.seckill.vo;

import com.janhen.seckill.pojo.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVO {
	/** 商品本身的信息. */
	private GoodsVO   goods;

	/** 所属订单信息. */
	private OrderInfo order;
}
