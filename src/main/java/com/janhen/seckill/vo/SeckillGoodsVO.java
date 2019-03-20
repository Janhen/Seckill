package com.janhen.seckill.vo;

import com.janhen.seckill.pojo.Goods;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SeckillGoodsVO extends Goods {

	/** 秒杀价与库存数与原始商品不同, 必须从 DB 获取. */
	private BigDecimal	seckillPrice;    // seckillPrice

	private Integer		stockCount;

	private Date		startDate;

	private Date		endDate;
}
