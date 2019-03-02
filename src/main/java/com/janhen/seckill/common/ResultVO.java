package com.janhen.seckill.common;

import lombok.Data;

@Data
public class ResultVO<T> {
    private int    code;
    private String msg;
    private T      data;

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(data);
    }

    public static <T> ResultVO<T> error(ResultEnum resultEnum) {
        return new ResultVO<>(resultEnum);
    }

    public static <T> ResultVO<T> error(int code, String msg) {
        return new ResultVO<>(code, msg);
    }

    public ResultVO(T data) {
        super();
        this.data = data;
    }

    public ResultVO(ResultEnum resultEnum) {
        if (resultEnum != null) {
            this.code = resultEnum.getCode();
            this.msg = resultEnum.getMsg();
        }
    }

    public ResultVO(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
}
