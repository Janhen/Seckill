package com.janhen.seckill.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeckillOrder {
  private Long id;
  private Long userId;
  private Long orderId;
  private Long goodsId;
}
