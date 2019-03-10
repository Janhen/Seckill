package com.janhen.seckill.common.rabbitmq;

import com.janhen.seckill.pojo.SeckillUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
	// 通过 DB 中 UNIQUE_INDEX 控制一个用户只能秒杀一件商品
	private SeckillUser user;
	private long        goodsId;
}
