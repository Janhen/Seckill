package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class SeckillKey extends BasePrefix{
	
	private SeckillKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static SeckillKey isGoodsOverByGid = new SeckillKey(Const.PERMANENT, "goodsOver");

	public static SeckillKey getSeckillPathByUidGid = new SeckillKey(5 * Const.MINUTE, "seckillPath");

	public static SeckillKey getSeckillVerifyCodeResultByUidGid = new SeckillKey(5 * Const.MINUTE, "verifyCode");
}
