package com.janhen.seckill.redis;

public class SeckillUserKey extends BasePrefix{

	// two hour
	public static final int TOKEN_EXPIRE = 3600 * 2;
	
	public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE, "token");
	public static SeckillUserKey getById = new SeckillUserKey(0, "id");
	
	private SeckillUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
