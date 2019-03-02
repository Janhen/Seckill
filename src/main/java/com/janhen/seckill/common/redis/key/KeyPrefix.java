package com.janhen.seckill.common.redis.key;

public interface KeyPrefix {
	
	int expireSeconds();
	
	String getPrefix();

}
