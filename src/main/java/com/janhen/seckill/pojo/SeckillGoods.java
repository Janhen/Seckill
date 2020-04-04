package com.janhen.seckill.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SeckillGoods {
  private Long id;
  private Long goodsId;
  private BigDecimal seckillPrice;
  private Integer stockCount;
  private Date startDate;
  private Date endDate;
}
