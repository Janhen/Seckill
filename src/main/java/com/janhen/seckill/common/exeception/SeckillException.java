package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;
import lombok.Getter;

@Getter
public class SeckillException extends RuntimeException {
	private static final long serialVersionUID = 55255L;

	private int code;

	public SeckillException(ResultEnum resultEnum) {
		super(resultEnum.getMsg());
		this.code = resultEnum.getCode();
	}

	public SeckillException(int code, String msg) {
		super(msg);
		this.code = code;
	}
}
