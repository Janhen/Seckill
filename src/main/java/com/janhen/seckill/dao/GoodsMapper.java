package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.SeckillGoods;
import com.janhen.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {
	
	@Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.seckill_price miaosha_price " + 
			"from goods g left join miaosha_goods mg on g.id = mg.goods_id")
	List<GoodsVo> selectGoodsVoList();
	
	@Select("select g.*, mg.seckill_price miaosha_goods, mg.stock_count, mg.start_date, mg.end_date " + 
			"from goods g left join miaosha_goods mg on g.id = mg.goods_id " + 
			"where g.id = #{goodsId}")
	GoodsVo selectGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
	
	@Update("update miaosha_goods " + 
			"set stock_count = stock_count -1 " + 
			"where goods_id = #{goodsId}")
	int updateStock(SeckillGoods miaoshaGoods);
}
