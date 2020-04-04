package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;

public class GoodsException extends SeckillException {
  private static final long serialVersionUID = 2285353042369954479L;

  public GoodsException(ResultEnum resultEnum) {
    super(resultEnum);
  }

  public GoodsException(int code, String msg) {
    super(code, msg);
  }
}
