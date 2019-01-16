package com.janhen.seckill.result;

public class ResultVO<T> {
    private int    code;
    private String msg;
    private T      data;

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(data);
    }

    public static <T> ResultVO<T> error(CodeMsg codeMsg) {
        return new ResultVO<T>(codeMsg);
    }

    public ResultVO(T data) {
        super();
        this.data = data;
    }

    public ResultVO(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    public ResultVO(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResultVO() {
        super();
    }

    public ResultVO(int code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
