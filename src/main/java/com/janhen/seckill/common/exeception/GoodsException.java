package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;

public class GoodsException extends SeckillException {

    public GoodsException(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public GoodsException(int code, String msg) {
        super(code, msg);
    }
}
