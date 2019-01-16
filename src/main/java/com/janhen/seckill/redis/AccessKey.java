package com.janhen.seckill.redis;

public class AccessKey extends BasePrefix{

	// use for test
	public static AccessKey access = new AccessKey(60, "access");
	
	private AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	// 访问限制流量使用
	public static AccessKey createByExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}

}
