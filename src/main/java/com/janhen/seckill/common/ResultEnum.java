package com.janhen.seckill.common;

import lombok.Getter;

@Getter
public enum ResultEnum {
	SUCCESS(0, "success"),
	
	// Common
	SERVER_ERROR(500100, "服务器端异常"),
	BIND_ERROR(500101, "参数校验异常: %s"),
	REQUEST_ILLEGAL(500102, "请求非法"),
	ACCESS_LIMIT_REACHED(500103, "访问过于频繁"),
	
	// Login 5002xx
	SESSION_ERROR(500210, "Session不存在或已经失效"),
	PASSWORD_EMPTY(500211, "登录密码不能为空"),
	MOBILE_EMPTY(500213, "手机密码不能为null"),
	MOBILE_NOT_EXIST(500214, "手机号不存在"),
	PASSWORD_ERROR(500215, "密码错误"),

	// Goods 5003xx
	GOODS_NOT_EXIST(500300, "商品不存在"),

	// Order  5004xx
	ORDER_NOT_EXIST(500400, "订单不存在"),

	// Seckill  5005xx
	SECKILL_OVER(500500, "商品已经秒杀完毕"),
	SECKILL_REPEATE(500501, "不能重复秒杀"),
	SECKILL_COUNT_LIMIT(500504, "秒杀达到限制"),
	SECKILL_FAIL(500503, "秒杀失败")
	;

	private int code;
	private String msg;
	
	ResultEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
