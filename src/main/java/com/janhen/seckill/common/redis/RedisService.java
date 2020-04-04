package com.janhen.seckill.common.redis;

import com.janhen.seckill.common.redis.key.KeyPrefix;
import com.janhen.seckill.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RedisService {

  @Autowired
  private JedisPool jedisPool;

  public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
    try (Jedis jedis = jedisPool.getResource()) {
      // generate the real key
      String realKey = prefix.getPrefix() + key;
      String str = jedis.get(realKey);
      T res = JSONUtil.stringToBean(str, clazz);
      return res;
    }
  }

  public <T> boolean set(KeyPrefix prefix, String key, T value) {
    try (Jedis jedis = jedisPool.getResource()) {
      String str = JSONUtil.beanToString(value);
      if (str == null || str.length() == 0) {
        return false;
      }
      String realKey = prefix.getPrefix() + key;
      int seconds = prefix.expireSeconds();
      if (seconds <= 0) {
        jedis.set(realKey, str);
      } else {
        jedis.setex(realKey, seconds, str);
      }
      return true;
    }
  }

  public Long incr(KeyPrefix prefix, String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      String realKey = prefix.getPrefix() + key;
      return jedis.incr(realKey);
    }
  }

  public Long decr(KeyPrefix prefix, String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      String realKey = prefix.getPrefix() + key;
      return jedis.decr(realKey);
    }
  }

  public boolean del(KeyPrefix prefix, String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      String realKey = prefix.getPrefix() + key;
      Long rowCount = jedis.del(realKey);
      return rowCount > 0;
    }
  }

  public boolean exists(KeyPrefix prefix, String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      String realKey = prefix.getPrefix() + key;
      return jedis.exists(realKey);
    }
  }

  /**
   * delete by prefix through scan prefix result
   *
   * @param prefix
   * @return
   */
  public boolean delete(KeyPrefix prefix) {
    if (prefix == null) {
      return false;
    }
    List<String> keys = scanKeys(prefix.getPrefix());
    if (keys == null || keys.size() <= 0) {
      return true;
    }
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      jedis.del(keys.toArray(new String[0]));
      return true;
    } catch (final Exception e) {
      log.error("【Redis】 删除失败", e);
      return false;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * scan cursor [match pattern] [count count]
   *
   * @param key
   * @return
   */
  public List<String> scanKeys(String key) {
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      List<String> keys = new ArrayList<>();
      String cursor = "0";
      // builder generate
      ScanParams sp = new ScanParams().match("*" + key + "*").count(100);
      do {
        ScanResult<String> ret = jedis.scan(cursor, sp);
        List<String> result = ret.getResult();
        if (result != null && result.size() > 0) {
          keys.addAll(result);
        }
        // new cursor to scan, as invariance
        cursor = ret.getStringCursor();
      } while (!cursor.equals("0"));
      return keys;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }
}
