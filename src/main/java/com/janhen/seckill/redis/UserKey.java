package com.janhen.seckill.redis;

public class UserKey extends BasePrefix{
	
	/*// 两天的过期时间
	public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
	
	private UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static UserKey toKey = new UserKey(TOKEN_EXPIRE, "tk");
	public static UserKey getById = new UserKey(0, "id");*/
	private UserKey(String prefix) {
		super(prefix);
	}
	public static UserKey getById = new UserKey("id");
	public static UserKey getByName = new UserKey("name");

}
