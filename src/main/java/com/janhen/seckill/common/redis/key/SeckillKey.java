package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class SeckillKey extends BasePrefix{
	
	private SeckillKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static SeckillKey isGoodsOverByGid = new SeckillKey(Const.PERMANENT, "goodsOver");

	public static SeckillKey getSeckillPathByUidGid = new SeckillKey(Const.MINUTE, "seckillPath");

	/** 某件商品的秒杀验证码. */
	public static SeckillKey getSeckillVerifyCodeResultByUidGid = new SeckillKey(5*Const.MINUTE, "verifyCode");
}
