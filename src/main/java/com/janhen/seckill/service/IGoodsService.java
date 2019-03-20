package com.janhen.seckill.service;

import com.janhen.seckill.vo.SeckillGoodsVO;

import java.util.List;

public interface IGoodsService {

    SeckillGoodsVO selectGoodsVoByGoodsId(Long goodsId);

    List<SeckillGoodsVO> selectSeckillGoodsVoList();

    boolean descStock(SeckillGoodsVO goods);
}
