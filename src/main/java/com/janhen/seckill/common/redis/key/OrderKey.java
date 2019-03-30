package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class OrderKey extends BasePrefix {

	/**
	 * use for querying order after seckill success
	 */
	public static OrderKey getSeckillOrderByUidGid = new OrderKey(1 * Const.DAY, "SeckillOrder" + Const.SPLIT);

	public OrderKey(String prefix) {
		super(prefix);
	}

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
