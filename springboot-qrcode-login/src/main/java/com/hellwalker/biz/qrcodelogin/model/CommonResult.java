package com.hellwalker.biz.qrcodelogin.model;

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
        this.code = 10001;
        this.data = data;
    }

    public CommonResult(CodeVO codeVO) {
        this.msg = codeVO.getMessage();
        this.code = 10001;
        this.data = codeVO;
    }
}
