package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;

public class LoginException extends SeckillException {
  private static final long serialVersionUID = -3767260321264166656L;

  public LoginException(ResultEnum resultEnum) {
    super(resultEnum);
  }
}
