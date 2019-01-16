package com.janhen.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
/** 将 JedisPool 注入到Spring中 */
public class RedisPoolFactory {
	
	@Autowired
	private RedisConfig redisConfig;
	
	@Bean
	public JedisPool JedisPoolFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait());
		
		JedisPool pool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(), 
				redisConfig.getTimeout() * 1000, redisConfig.getPassword(), 0);
		return pool;
		
	}

}
