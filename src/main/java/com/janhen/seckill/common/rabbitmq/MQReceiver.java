package com.janhen.seckill.common.rabbitmq;

import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.IOrderService;
import com.janhen.seckill.service.ISeckillService;
import com.janhen.seckill.util.JSONUtil;
import com.janhen.seckill.vo.SeckillGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQReceiver {

	@Autowired
	RedisService redisService;
	
	@Autowired
	IGoodsService iGoodsService;
	
	@Autowired
	IOrderService iOrderService;
	
	@Autowired
	ISeckillService iSeckillService;
	
	@RabbitListener(queues={MQConfig.SECKILL_QUEUE})
	public void receiveSeckillMessage(String message) {
		log.info("【消息队列】receive message : " + message);
		
		SeckillMessage seckillMessage = JSONUtil.stringToBean(message, SeckillMessage.class);
		SeckillUser user = seckillMessage.getUser();
		long goodsId = seckillMessage.getGoodsId();

		SeckillGoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
		Integer stock = goods.getStockCount();
		if (stock == null || stock <= 0) {          // already have not stock
			return;
		}
		if (user == null || goods == null) {
			log.error("【消息队列】传入参数有误 {}, {}", user, goodsId);
			return ;
		}
		// already seckill, judge two time;  First time: request seckill; Second time:
		SeckillOrder order = iOrderService.selectSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {
			return;
		}

		// control seckill and unique
		iSeckillService.seckill(user, goods);
		log.info("入库成功！！");
	}
}
