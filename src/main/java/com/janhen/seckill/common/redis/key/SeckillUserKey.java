package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class SeckillUserKey extends BasePrefix{

	/**
	 * user for distributed session
	 */
	public static SeckillUserKey token = new SeckillUserKey(Const.TOKEN_EXPIRE, "token" + Const.SPLIT);
	/**
	 * Object level cache
	 */
	public static SeckillUserKey getById = new SeckillUserKey(Const.PERMANENT, "id" + Const.SPLIT);
	
	private SeckillUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
