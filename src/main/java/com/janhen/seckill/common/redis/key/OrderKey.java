package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class OrderKey extends BasePrefix {

	public static OrderKey getSeckillOrderByUidGid = new OrderKey(Const.DAY, "SeckillOrder" + Const.SPLIT);

	public OrderKey(String prefix) {
		super(prefix);
	}

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
