package com.janhen.seckill.common.rabbitmq;

import com.janhen.seckill.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQSender {

	@Autowired
	AmqpTemplate amqpTemplate;
	
	public void sendSeckillMessage(SeckillMessage message) {
		String msg = JSONUtil.beanToString(message);
		log.info("【消息队列】Send message : {}", msg);

		amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
	}

	public void sendSeckillCountLimitMessage(SeckillCountLimitMessage message) {
		String msg = JSONUtil.beanToString(message);
		log.info("【消息队列】Send message: {}", msg);

		amqpTemplate.convertAndSend(MQConfig.SECKILL_COUNT_LIMIT_QUEUE, msg);
	}
}
