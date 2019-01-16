package com.janhen.seckill.vo;

import com.janhen.seckill.pojo.SeckillUser;

public class GoodsDetailVo {

	private int         seckillStatus	= 0;
	private int         remainSeconds	= 0;
	private SeckillUser user;
	private GoodsVo     goods;
	
	public GoodsDetailVo(int seckillStatus, int remainSeconds, SeckillUser user, GoodsVo goods) {
		super();
		this.seckillStatus = seckillStatus;
		this.remainSeconds = remainSeconds;
		this.user = user;
		this.goods = goods;
	}
	public GoodsDetailVo() {
		super();
	}
	public int getSeckillStatus() {
		return seckillStatus;
	}
	public void setSeckillStatus(int seckillStatus) {
		this.seckillStatus = seckillStatus;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	public SeckillUser getUser() {
		return user;
	}
	public void setUser(SeckillUser user) {
		this.user = user;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}

}
