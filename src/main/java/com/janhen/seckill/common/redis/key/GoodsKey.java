package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class GoodsKey extends BasePrefix{

	public static final GoodsKey getGoodsList = new GoodsKey(Const.MINUTE, "goodslist");
	public static final GoodsKey getGoodsDetail = new GoodsKey(Const.MINUTE, "goodsdetail");
	public static GoodsKey getSeckillGoodsStock = new GoodsKey(Const.PERMANENT, "goodsStock");
	
	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
