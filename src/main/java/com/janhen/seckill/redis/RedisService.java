package com.janhen.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.janhen.seckill.util.WebUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

	@Autowired
	private JedisPool jedisPool;

	/** get and set from redis cache by prefix and key */
	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {

		try (Jedis jedis = jedisPool.getResource();) {

			// generate the real key
			String realKey = prefix.getPrefix() + key;
			String str = jedis.get(realKey);
			T res = WebUtil.stringToBean(str, clazz);

			return res;
		} 
	}

	public <T> boolean set(KeyPrefix prefix, String key, T value) {

		try (Jedis jedis = jedisPool.getResource();) {
			String str = WebUtil.beanToString(value);
			if (str == null || str.length() == 0) {
				return false;
			}

			String realKey = prefix.getPrefix() + key;
			int seconds = prefix.expireSeconds();
			// <0 set expire time to permanent(default)
			if (seconds <= 0) {
				jedis.set(realKey, str);
			} else {
				jedis.setex(realKey, seconds, str);
			}

			return true;
		} 
	}
	
	public boolean del(KeyPrefix prefix, String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			String realKey = prefix.getPrefix() + key;
			Long rowCount = jedis.del(realKey);
			return rowCount > 0;
		} 
	}

	public <T> boolean exists(KeyPrefix prefix, String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			String realKey = prefix.getPrefix() + key;
			return jedis.exists(realKey);
		} 
	}

	/** ++ -- operation */
	public <T> Long incr(KeyPrefix prefix, String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			String realKey = prefix.getPrefix() + key;
			return jedis.incr(realKey);
		} 
	}

	public <T> Long decr(KeyPrefix prefix, String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			String realKey = prefix.getPrefix() + key;
			return jedis.decr(realKey);
		} 
	}

	
	
	// ----------------------------AUX-----------------------
	/*public void returnToPool(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}*/
	/*public static <T> T stringToBean(String str, Class<T> clazz) {
		if (str == null || str.length() == 0 || clazz == null) 
			return null;

		// int 和 long 基本类型进行特殊处理, 无法使用 json 工具转换
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(str);
		} else if (clazz == String.class) {
			return (T) str;
		} else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}

	public static <T> String beanToString(T value) {
		if (value == null) 
			return null;

		Class<?> clazz = value.getClass();

		if (clazz == int.class || clazz == Integer.class) {
			return String.valueOf(value);
		} else if (clazz == long.class || clazz == Long.class) {
			return String.valueOf(value);
		} else if (clazz == String.class) {
			return (String) value;
		} else {
			return JSON.toJSONString(value);
		}
	}*/

	

}
