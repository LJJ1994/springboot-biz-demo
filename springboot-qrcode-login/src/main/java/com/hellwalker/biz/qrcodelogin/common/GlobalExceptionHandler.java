package com.hellwalker.biz.qrcodelogin.common;

import com.hellwalker.biz.qrcodelogin.model.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public CommonResult handleBizException(BizException e){
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResult hanldeSysException(Exception e) {
        return CommonResult.failed(e.getMessage());
    }
}
