package com.janhen.seckill.common.rabbitmq;

import com.janhen.seckill.pojo.SeckillUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillCountLimitMessage {
    // 一个用户可多次秒杀同一件商品, 具体策略根据业务选择
    private SeckillUser user;
    private long goodsId;

    // 秒杀的商品数量, 用户可秒杀一次性秒杀同一件商品多次的业务逻辑
//     private int count;
}
