package com.janhen.seckill.interceptor;

import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.pojo.SeckillUser;

public class UserContext {
	// threadlocal implement transfer param : interceptor --->> 
	private static ThreadLocal<SeckillUser> userHolder = new ThreadLocal<>();
	
	public static void setUser(SeckillUser user) {
		userHolder.set(user);
	}
	
	public static SeckillUser getUser() {
		return userHolder.get();
	}
}
