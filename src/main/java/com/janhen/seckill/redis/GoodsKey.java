package com.janhen.seckill.redis;

public class GoodsKey extends BasePrefix{

	public static final GoodsKey getGoodsList = new GoodsKey(60, "goodslist");
	public static final GoodsKey getGoodsDetail = new GoodsKey(60, "goodsdetail");
	public static GoodsKey getSeckillGoodsStock = new GoodsKey(0, "goodsStock");
	
	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
