package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {
  String TABLE_ORDER_INFO = " order_info ";
  String TABLE_SECKILL_ORDER = " seckill_order ";

  @Insert("INSERT INTO order_info "
          + "VALUES (null, #{userId}, #{goodsId}, null, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate}, null)")
  @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
  long insertOrderInfo(OrderInfo orderInfo);

  @Insert("INSERT INTO seckill_order(user_id, order_id, goods_id) " + "VALUES(#{userId}, #{orderId}, #{goodsId})")
  int insertSeckillOrder(SeckillOrder seckillOrder);

  @Select("SELECT * " + "FROM seckill_order " + "WHERE user_id = #{userId} AND goods_id = #{goodsId}")
  SeckillOrder selectSeckillOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

  @Select("SELECT *  FROM order_info  WHERE id = #{orderId}")
  OrderInfo selectOrderInfoById(@Param("orderId") Long orderId);
}
