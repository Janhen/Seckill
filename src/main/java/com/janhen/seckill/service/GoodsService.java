package com.janhen.seckill.service;

import java.util.List;

import com.janhen.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.janhen.seckill.dao.GoodsMapper;
import com.janhen.seckill.pojo.SeckillGoods;
import com.janhen.seckill.vo.GoodsVo;

@Service
public class GoodsService {
	
	@Autowired
	GoodsMapper goodsDao;

	public GoodsVo selectGoodsVoByGoodsId(Long goodsId) {
		if (goodsId == null) {
			throw new IllegalArgumentException();
		}
		GoodsVo goodsVo = goodsDao.selectGoodsVoByGoodsId(goodsId);
		if (goodsVo == null) {
			throw new RuntimeException("db 中无该" + goodsId);
		}
		return goodsVo;
	}

	public List<GoodsVo> selectGoodsVoList() {
		List<GoodsVo> goodsList = goodsDao.selectGoodsVoList();
		return goodsList;
	}
	
	public boolean descStock(GoodsVo goods) {
		SeckillGoods miaoshaGoods = new SeckillGoods();
		miaoshaGoods.setGoodsId(goods.getId());
		int rowCount = goodsDao.updateStock(miaoshaGoods);
		return rowCount > 0 ? true : false;
	}
	
	

}
