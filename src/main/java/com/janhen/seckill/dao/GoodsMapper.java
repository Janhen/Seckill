package com.janhen.seckill.dao;

import com.janhen.seckill.vo.SeckillGoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {

	String TABLE_SECKILL_GOODS = " seckill_goods ";

	@Select("SELECT g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price seckill_price " +
			"FROM goods g INNER JOIN seckill_goods sg ON g.id = sg.goods_id")
	List<SeckillGoodsVO> selectSeckillGoodsVoList();

	@Select("SELECT g.*, sg.seckill_price, sg.stock_count, sg.start_date, sg.end_date " +
			"FROM goods g LEFT JOIN seckill_goods sg ON g.id = sg.goods_id " +
			"WHERE g.id = #{goodsId}")
	SeckillGoodsVO selectGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
	
	@Update({"UPDATE ", TABLE_SECKILL_GOODS, "SET stock_count=stock_count-1 WHERE goods_id=#{goodsId} AND stock_count > 0"})
	int updateSeckillStock(@Param("goodsId") Long goodsId);
}
