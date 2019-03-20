package com.janhen.seckill.common.rabbitmq;

import com.janhen.seckill.pojo.SeckillUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
	private SeckillUser user;
	private long        goodsId;
}
