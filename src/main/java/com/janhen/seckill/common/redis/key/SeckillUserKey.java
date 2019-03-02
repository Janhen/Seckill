package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class SeckillUserKey extends BasePrefix{

	// two hour
	public static SeckillUserKey token = new SeckillUserKey(Const.TOKEN_EXPIRE, "token");
	public static SeckillUserKey getById = new SeckillUserKey(Const.PERMANENT, "id");
	
	private SeckillUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
