package com.janhen.seckill.common.rabbitmq;

import com.janhen.seckill.common.Const;
import com.janhen.seckill.common.redis.RedisService;
import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.IOrderService;
import com.janhen.seckill.service.ISeckillService;
import com.janhen.seckill.util.JSONUtil;
import com.janhen.seckill.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {
	
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

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

		GoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
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
		
		iSeckillService.seckill(user, goods);
		log.info("入库成功！！");
	}

	@RabbitListener(queues = {MQConfig.SECKILL_COUNT_LIMIT_QUEUE})
	public void receiveSeckillCountLimitMessage(String message) {
		log.info("【消息队列】Receive message: {}", message);

		SeckillCountLimitMessage seckillCountLimitMessage = JSONUtil.stringToBean(message, SeckillCountLimitMessage.class);
		SeckillUser user = seckillCountLimitMessage.getUser();
		long goodsId = seckillCountLimitMessage.getGoodsId();

		GoodsVO goods = iGoodsService.selectGoodsVoByGoodsId(goodsId);
		Integer stock = goods.getStockCount();
		if (stock == null || stock <= 0) {
			log.info("【消息队列】用户: {}, 排队后无商品: {} 可供秒杀", user.getId(), goodsId);
			return ;
		}
		// judge Because multi thread, user time interval
		Integer seckillCnt = iOrderService.selectSeckillCountByUserIdAndGoodsId(user.getId(), goodsId);
		if (seckillCnt > Const.MAX_SECKILL_COUNT) {
			log.error("【消息队列】用户: {}, 秒杀商品: {}, 达到上限", user.getId(), goodsId);
		}

		iSeckillService.seckill(user, goods);
		log.info("入库成功!!");
	}
}
