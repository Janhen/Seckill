package com.janhen.seckill.service;

import com.janhen.seckill.vo.GoodsVO;

import java.util.List;

public interface IGoodsService {

    GoodsVO selectGoodsVoByGoodsId(Long goodsId);

    List<GoodsVO> selectGoodsVoList();

    List<GoodsVO> selectSeckillGoodsVoList();

    boolean descStock(GoodsVO goods);
}
