package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public abstract class BasePrefix implements KeyPrefix{

	private int expireSeconds;

	private String prefix;

	public static final String EMPTY_KEY = "";
	
	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	public BasePrefix(String prefix) {
		this(Const.PERMANENT, prefix);
	}

	// key generate

	public static String getKey(Long arg1) {
		return arg1.toString();
	}

	public static String getKey(Long arg1, Long arg2) {
		return arg1 + Const.SPLIT + arg2;
	}

	public static String getKey(String ... ags) {
		StringBuilder sb = new StringBuilder();
		for (String key : ags)
			sb.append(key).append(Const.SPLIT);
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public static String getKey(Object ... ags) {
		StringBuilder sb = new StringBuilder();
		for (Object key : ags)
			sb.append(key.toString()).append(Const.SPLIT);
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	@Override
	public int expireSeconds() {
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		String className = this.getClass().getSimpleName();
		return new StringBuffer().append(className).append(Const.SPLIT).append(prefix).toString();
	}
}
