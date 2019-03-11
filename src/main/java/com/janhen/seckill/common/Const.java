package com.janhen.seckill.common;

import lombok.Getter;

public class Const {

    public final static int MINUTE = 60;

    public final static int HOUR = 3600;

    public final static int DAY = 86400;

    public final static int PERMANENT = 0;

    public static final int TOKEN_EXPIRE = 3600 * 2;

    public final static String SALT = "1a2b3c4d";

    public final static String COOKIE_NAME_TOKEN = "token";

    public final static String SPLIT = ":";

    public final static int MAX_SECKILL_COUNT = 5;

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

    @Getter
    public enum SeckillStatusEnum {

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

    @Getter
    public enum OrderStatus {
        NOT_PAY(0, "未支付"),
        ALREADY_PAY(1, "已支付"),
        HAS_SHIPPED(2, "已发货"),
        HAS_RECEIVED(3, "已收货"),
        HAS_REFUND(4, "已退款"),
        HAS_OVER(5, "已完成");

        private int code;

        private String msg;

        OrderStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
