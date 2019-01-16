package com.janhen.seckill.vo;

import com.janhen.seckill.pojo.Goods;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsVo extends Goods {

	private BigDecimal	seckillPrice;
	private Integer		stockCount;
	private Date		startDate;
	private Date		endDate;
	
	public BigDecimal getSeckillPrice() {
		return seckillPrice;
	}
	public void setSeckillPrice(BigDecimal miaoshaPrice) {
		this.seckillPrice = miaoshaPrice;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
