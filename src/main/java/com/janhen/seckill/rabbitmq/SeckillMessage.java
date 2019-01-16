package com.janhen.seckill.rabbitmq;

import com.janhen.seckill.pojo.SeckillUser;

public class SeckillMessage {
	
	private SeckillUser user;
	private long        goodsId;
	
	public SeckillMessage(SeckillUser user, long goodsId) {
		super();
		this.user = user;
		this.goodsId = goodsId;
	}
	public SeckillMessage() {
		super();
	}
	public SeckillUser getUser() {
		return user;
	}
	public void setUser(SeckillUser user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
