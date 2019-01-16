package com.janhen.seckill.redis;

public class AccessKey extends BasePrefix{

	// use to test
	public static AccessKey access = new AccessKey(5, "access");
	
	private AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static AccessKey createByExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}

}
