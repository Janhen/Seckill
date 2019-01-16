package com.janhen.seckill.rabbitmq;

import com.janhen.seckill.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

	@Autowired
	AmqpTemplate amqpTemplate;
	
	public void send(Object message) {
		String msg = JSONUtil.beanToString(message);
		log.info("send message : " + msg);
		
		amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
	}
}
