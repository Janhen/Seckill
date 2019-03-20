package com.janhen.seckill.controller.interceptor;

import com.janhen.seckill.pojo.SeckillUser;

public class UserContext {
	private static ThreadLocal<SeckillUser> userHolder = new ThreadLocal<>();
	
	public static void setUser(SeckillUser user) {
		userHolder.set(user);
	}
	
	public static SeckillUser getUser() {
		return userHolder.get();
	}

	public static void deleteUser() {
		userHolder.remove();
	}
}
