package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;

public class OrderException extends SeckillException {
    public OrderException(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public OrderException(int code, String msg) {
        super(code, msg);
    }
}
