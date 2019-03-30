package com.janhen.seckill.common.redis.key;

public class AccessKey extends BasePrefix{

	// use for test
	public static AccessKey access = new AccessKey(60, "access");
	
	private AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	/**
	 * use for user defined @AccessLimit annotation
	 */
	public static AccessKey createByExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "accessLimit");
	}
}
