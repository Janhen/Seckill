package com.janhen.seckill.common.redis.key;

public class AccessKey extends BasePrefix {
  public static AccessKey access = new AccessKey(60, "access");

  private AccessKey(int expireSeconds, String prefix) {
    super(expireSeconds, prefix);
  }

  public static AccessKey createByExpire(int expireSeconds) {
    return new AccessKey(expireSeconds, "AccessLimit");
  }
}
