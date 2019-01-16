package com.janhen.seckill.result;

public class CodeMsg {
	
	public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
	
	// 通用异常
	public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务器端异常");
	public static final CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常: %s");
	public static final CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
	public static final CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500103, "访问过于频繁");
	
	
	// 登录模块 5002xx
	public static final CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或已经失效");
	public static final CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
	public static final CodeMsg MOBILE_EMPTY = new CodeMsg(500213, "手机密码不能为null");
	public static final CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
	public static final CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
	
	// 商品模块 5003xx
	
	//订单模块  5004xx
	public static final CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");
	
	
	// 秒杀模块  5005xx
	public static final CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
	public static final CodeMsg SECKILL_REPEATE = new CodeMsg(500501, "不能重复秒杀");
	public static final CodeMsg SECKILL_FAIL = new CodeMsg(500503, "秒杀失败");
	
	private int code;
	private String msg;
	
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	/** C0. ??扩充方法, code 默认为0 */
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String msg = String.format(this.msg, args);
		return new CodeMsg(code, msg);
	}
}
