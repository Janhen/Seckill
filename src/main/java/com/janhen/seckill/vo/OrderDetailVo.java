package com.janhen.seckill.vo;

import com.janhen.seckill.pojo.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVO {
	private GoodsVO   goods;
	private OrderInfo order;
	
}
