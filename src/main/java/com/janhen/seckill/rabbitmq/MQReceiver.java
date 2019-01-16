package com.janhen.seckill.rabbitmq;

import com.janhen.seckill.pojo.SeckillOrder;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.redis.RedisService;
import com.janhen.seckill.service.impl.GoodsServiceImpl;
import com.janhen.seckill.service.impl.OrderServiceImpl;
import com.janhen.seckill.service.impl.SeckillServiceImpl;
import com.janhen.seckill.util.JSONUtil;
import com.janhen.seckill.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 配置消息队列 amqp 协议规定的。。。
 * @author Janh
 *
 */
@Component
public class MQReceiver {
	
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
	
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsServiceImpl goodsServiceImpl;
	
	@Autowired
	OrderServiceImpl orderServiceImpl;
	
	@Autowired
	SeckillServiceImpl seckillService;
	
	/**
	 * Direct 模式 ： 交换机 Exchange
	 */
	// @RabbitListener(queues=MQConfig.SECKILL_QUEUE)
	public void receive(String message) {
		log.info("receive message : " + message);
		
		SeckillMessage seckillMessage = JSONUtil.stringToBean(message, SeckillMessage.class);
		SeckillUser user = seckillMessage.getUser();
		long goodsId = seckillMessage.getGoodsId();
		
		GoodsVO goods = goodsServiceImpl.selectGoodsVoByGoodsId(goodsId);
		Integer stock = goods.getStockCount();
		if (stock == null || stock <= 0) {
			return;
		}
		
		
		SeckillOrder order = orderServiceImpl.selectSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
		if (order != null) {
			return;
		}
		
		seckillService.seckill(user, goods);
		log.info("入库成功！！");
	}
	
	
	
	

}
