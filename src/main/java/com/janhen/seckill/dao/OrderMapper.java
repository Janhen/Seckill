package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {

	@Select("select * " + "from miaosha_order " + "where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder selectMiaoshaOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	@Insert("insert into order_info "
			+ "values (null, #{userId}, #{goodsId}, null, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate}, null)")
	@SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
	long insertOrderInfo(OrderInfo orderInfo);

	@Insert("insert into miaosha_order(user_id, order_id, goods_id) " + "values(#{userId}, #{orderId}, #{goodsId})")
	int insertSeckillOrder(SeckillOrder miaoshaOrder);

	@Select("select *  from order_info  where id = #{orderId}")
	OrderInfo selectOrderInfoById(@Param("orderId") Long orderId);
	
}
