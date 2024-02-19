package com.hellwalker.common.exception;

public class BizException extends RuntimeException{
    private String msg;

    private int code;


    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String msg, int code, String message) {
        super(message);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
