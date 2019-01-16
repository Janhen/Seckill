package com.janhen.seckill.redis;

public interface KeyPrefix {
	
	int expireSeconds();
	
	String getPrefix();

}
