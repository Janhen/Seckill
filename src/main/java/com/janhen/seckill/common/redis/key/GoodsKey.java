package com.janhen.seckill.common.redis.key;

import com.janhen.seckill.common.Const;

public class GoodsKey extends BasePrefix{

	/**
	 * use for cache list, key expire update strategy
	 */
	public static final GoodsKey getGoodsList = new GoodsKey(Const.MINUTE, "goodslist");

	/**
	 * can cache some hot goods
	 */
	public static final GoodsKey getGoodsDetailByGid = new GoodsKey(Const.MINUTE, "goodsdetail" + Const.SPLIT);

	/**
	 * use for reducing stock ...
 	 */
	public static GoodsKey getSeckillGoodsStockByGid = new GoodsKey(Const.PERMANENT, "goodsStock" + Const.SPLIT);
	
	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
}
