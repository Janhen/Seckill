package com.janhen.seckill.common;

import lombok.Getter;

@Getter
public enum SeckillStatusEnum {

    // 0 NOT_BEGIN
    // 1 ON_SECKILL
    // 2 OVER
    NOT_BEGIN(0, "秒杀未开始"),
    ON(1, "秒杀进行中"),
    OVER(2, "秒杀已经结束")
    ;

    private int code;

    private String msg;

    SeckillStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
