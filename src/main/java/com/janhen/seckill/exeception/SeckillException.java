package com.janhen.seckill.exeception;

import com.janhen.seckill.result.CodeMsg;

public class SeckillException extends RuntimeException {
	private static final long serialVersionUID = 55255L;
	
	private CodeMsg codeMsg;
	
	public SeckillException(CodeMsg codeMsg) {
		super(codeMsg.getMsg());
		this.codeMsg = codeMsg;
	}

	public SeckillException() {
		super();
	}
	
	public CodeMsg getCodeMsg() {
		return codeMsg;
	}
}
