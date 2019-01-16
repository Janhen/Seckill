package com.janhen.seckill.redis;

public abstract class BasePrefix implements KeyPrefix{

	private int expireSeconds;

	private String prefix;
	
	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	public BasePrefix(String prefix) {
		this(0, prefix);
	}

	@Override
	public int expireSeconds() {
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		String className = this.getClass().getSimpleName();
		// 标识每个键所属模块, 及所属模块下的子层次
		return new StringBuffer().append(className).append(":").append(prefix).toString();
	}
}
