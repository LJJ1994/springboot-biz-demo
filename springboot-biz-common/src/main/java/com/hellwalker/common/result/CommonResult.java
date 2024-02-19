package com.hellwalker.common.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommonResult {
    private String msg;

    private int code;

    private Object data;

    public CommonResult(String msg, int code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public CommonResult(String msg, int code) {
        this.msg = msg;
        this.code = code;
        this.data = null;
    }

    public CommonResult(Object data) {
        this.msg = "success";
        this.code = 10000;
        this.data = data;
    }

    public static CommonResult success(Object data) {
        return new CommonResult("success", 10000, data);
    }

    public static CommonResult success(String msg) {
        return new CommonResult(msg, 10000, null);
    }

    public static CommonResult success(String msg, int code, Object data) {
        return new CommonResult(msg, code, data);
    }

    public static CommonResult failed(Object data) {
        return new CommonResult("failed", 20000, data);
    }

    public static CommonResult failed(String msg) {
        return new CommonResult(msg, 20000, null);
    }

    public static CommonResult failed(String msg, int code, Object data) {
        return new CommonResult(msg, code, data);
    }
}
