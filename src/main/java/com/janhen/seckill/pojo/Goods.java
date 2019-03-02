package com.janhen.seckill.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Janh
 *
 */
@Data
public class Goods {

	private Long		id;

	private String		goodsName;

	private String		goodsTitle;

	private String		goodsImg;

	private String		goodsDetail;

	private BigDecimal	goodsPrice;

	private Integer		goodsStock;
	
}
