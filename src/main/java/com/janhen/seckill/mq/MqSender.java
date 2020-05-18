package com.janhen.seckill.mq;

import com.janhen.seckill.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

import static com.janhen.seckill.util.JSONUtil.*;

@Slf4j
public class MqSender {

	public static void sendSeckillMessage(SeckillMessage message) {
		AmqpTemplate amqpTemplate = SpringContextUtil.getBean(AmqpTemplate.class);
		String msg = beanToString(message);
		log.info("【消息队列】Send message: {}", msg);

		amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
	}
}
