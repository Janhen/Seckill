package com.janhen.seckill.common;

import lombok.Getter;

@Getter
public enum SeckillOrderStatusEnum {

    OVER(-1L, "订单结束"),
    WAIT_ON_QUEUE(0L, "排队中");

    private long code;

    private String msg;

    SeckillOrderStatusEnum(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
