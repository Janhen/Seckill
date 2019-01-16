package com.janhen.seckill.redis;

public abstract class BasePrefix implements KeyPrefix{

	private int expireSeconds;
	private String prefix;
	
	public BasePrefix() { }
	
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
		return new StringBuffer().append(className).append(":").append(prefix).toString();
	}
}
