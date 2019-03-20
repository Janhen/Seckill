package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class GoodsKey extends BasePrefix{

	public static final GoodsKey getGoodsList = new GoodsKey(Const.MINUTE, "goodslist");
	public static final GoodsKey getGoodsDetailByGid = new GoodsKey(Const.MINUTE, "goodsdetail");
	public static GoodsKey getSeckillGoodsStockByGid = new GoodsKey(Const.PERMANENT, "goodsStock" + Const.SPLIT);
	
	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
