package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

	String TABLE_ORDER_INFO = "order_info";

	String TABLE_SECKILL_ORDER = "seckill_order";

	String INSERT_FIELD = "user_id, order_id, goods_id";

	@Select("select * " + "from seckill_order " + "where user_id = #{userId} and goods_id = #{goodsId} order by id limit 1")
    SeckillOrder selectSeckillOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	@Select("select * " + "from seckill_order " + "where user_id = #{userId} and goods_id = #{goodsId}")
	List<SeckillOrder> selectSeckillOrderByUserIdAndGoodsIdForCountLimit(@Param("userId") long userId, @Param("goodsId") long goodsId);

	@Select({"SELECT COUNT( * ) FROM ", TABLE_SECKILL_ORDER, " WHERE user_id=#{userId} AND goods_id=#{goodsId}"})
	int selectSeckillCountByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	@Insert("insert into order_info "
			+ "values (null, #{userId}, #{goodsId}, null, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate}, null)")
	@SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
	long insertOrderInfo(OrderInfo orderInfo);

	@Insert("insert into seckill_order(user_id, order_id, goods_id) " + "values(#{userId}, #{orderId}, #{goodsId})")
	int insertSeckillOrder(SeckillOrder seckillOrder);

	@Select("select *  from order_info  where id = #{orderId}")
	OrderInfo selectOrderInfoById(@Param("orderId") Long orderId);
	
}
