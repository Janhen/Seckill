package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public abstract class BasePrefix implements KeyPrefix{

	private int expireSeconds;

	private String prefix;
	
	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	public BasePrefix(String prefix) {
		this(Const.PERMANENT, prefix);
	}

	@Override
	public int expireSeconds() {
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		String className = this.getClass().getSimpleName();
		return new StringBuffer().append(className).append(Const.SPLIT).append(prefix).append(Const.SPLIT).toString();
	}
}
