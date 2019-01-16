package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.SeckillGoods;
import com.janhen.seckill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {
	
	@Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price seckill_price " +
			"from goods g left join seckill_goods sg on g.id = sg.goods_id")
	List<GoodsVO> selectGoodsVoList();
	
	@Select("select g.*, sg.seckill_price seckill_goods, sg.stock_count, sg.start_date, sg.end_date " +
			"from goods g left join seckill_goods sg on g.id = sg.goods_id " +
			"where g.id = #{goodsId}")
	GoodsVO selectGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
	
	@Update("update seckill_goods " +
			"set stock_count = stock_count -1 " + 
			"where goods_id = #{goodsId}")
	int updateStock(SeckillGoods seckillGoods);
}
