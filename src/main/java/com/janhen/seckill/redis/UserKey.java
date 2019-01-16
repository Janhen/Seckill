package com.janhen.seckill.redis;

public class UserKey extends BasePrefix{
	
	private UserKey(String prefix) {
		super(prefix);
	}
	public static UserKey getById = new UserKey("id");
	public static UserKey NAME = new UserKey("name");

}
