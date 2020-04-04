package com.janhen.seckill.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OrderInfo {
  private Long id;
  private Long userId;
  private Long goodsId;
  private Long deliveryAddrId;
  private String goodsName;
  private Integer goodsCount;
  private BigDecimal goodsPrice;
  private Integer orderChannel;
  private Integer status;
  private Date createDate;
  private Date payDate;
}
