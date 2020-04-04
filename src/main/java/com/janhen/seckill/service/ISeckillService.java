package com.janhen.seckill.service;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.vo.SeckillGoodsVO;

import java.awt.image.BufferedImage;

public interface ISeckillService {

  OrderInfo seckill(SeckillUser user, SeckillGoodsVO goods);

  String generateSeckillPath(SeckillUser user, Long goodsId);

  BufferedImage generateVerfiyCodeImg(SeckillUser user, Long goodsId);

  boolean checkVerifyCode(SeckillUser user, Long goodsId, Integer verfiyCode);

  Long getSeckillResult(Long userId, Long goodsId);

  boolean checkPath(SeckillUser user, Long goodsId, String path);
}
