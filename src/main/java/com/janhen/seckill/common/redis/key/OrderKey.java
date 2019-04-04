package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class OrderKey extends BasePrefix {

	/**
	 * object cache: like a dp table key(userId,goodsId) val(seckillOrder)
	 * can keep in cache util this seckillGoods time over
	 */
	public static OrderKey getSeckillOrderByUidGid = new OrderKey(1 * Const.DAY, "seckillOrder" + Const.SPLIT);

	public OrderKey(String prefix) {
		super(prefix);
	}

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
