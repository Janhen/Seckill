package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;

public class LoginException extends SeckillException {

    public LoginException(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
