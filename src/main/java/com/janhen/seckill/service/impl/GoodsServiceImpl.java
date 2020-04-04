package com.janhen.seckill.service.impl;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.exeception.GoodsException;
import com.janhen.seckill.dao.GoodsMapper;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.vo.SeckillGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements IGoodsService {

  @Autowired
  private GoodsMapper goodsMapper;

  @Override
  public SeckillGoodsVO selectGoodsVoByGoodsId(Long goodsId) {
    if (goodsId == null) {
      throw new IllegalArgumentException();
    }
    SeckillGoodsVO seckillGoodsVO = goodsMapper.selectGoodsVoByGoodsId(goodsId);
    if (seckillGoodsVO == null) {
      log.error("【商品查询】无此商品 goodsId: {}", goodsId);
      throw new GoodsException(ResultEnum.GOODS_NOT_EXIST);
    }
    return seckillGoodsVO;
  }

  @Override
  public List<SeckillGoodsVO> selectSeckillGoodsVoList() {
    List<SeckillGoodsVO> goodsList = goodsMapper.selectSeckillGoodsVoList();
    return goodsList;
  }

  @Override
  public boolean descStock(SeckillGoodsVO goods) {
    int rowCount = goodsMapper.updateSeckillStock(goods.getId());
    return rowCount > 0 ? true : false;
  }
}
