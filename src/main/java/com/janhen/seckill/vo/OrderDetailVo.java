package com.janhen.seckill.vo;

import com.janhen.seckill.pojo.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
  private SeckillGoodsVO goods;

  private OrderInfo order;
}
