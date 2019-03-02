package com.janhen.seckill.service.impl;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.dao.GoodsMapper;
import com.janhen.seckill.common.exeception.GoodsException;
import com.janhen.seckill.pojo.SeckillGoods;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements IGoodsService {
	
	@Autowired
	GoodsMapper goodsDao;

	public GoodsVO selectGoodsVoByGoodsId(Long goodsId) {
		if (goodsId == null) {
			throw new IllegalArgumentException();
		}
		GoodsVO goodsVO = goodsDao.selectGoodsVoByGoodsId(goodsId);
		if (goodsVO == null) {
			log.error("【商品查询】无此商品 goodsId: {}", goodsId);
			throw new GoodsException(ResultEnum.GOODS_NOT_EXIST);
		}
		return goodsVO;
	}

	public List<GoodsVO> selectGoodsVoList() {
		List<GoodsVO> goodsList = goodsDao.selectGoodsVoList();
		return goodsList;
	}
	
	public boolean descStock(GoodsVO goods) {
		SeckillGoods seckillGoods = new SeckillGoods();
		seckillGoods.setGoodsId(goods.getId());
		int rowCount = goodsDao.updateStock(seckillGoods);
		return rowCount > 0 ? true : false;
	}
}
