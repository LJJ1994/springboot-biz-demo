package com.hellwalker.biz.weixinlogin.controller;

import com.hellwalker.common.result.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * 返回json格式的错误信息
     * @return
     */
    @RequestMapping("/api/error")
    @ResponseBody
    public CommonResult handleError() {
        return CommonResult.failed("系统错误");
    }

    /**
     * 返回错误页面
     * @return
     */
    @RequestMapping("/page/error")
    public String pageError() {
        return "page_error";
    }

    @Override
    public String getErrorPath() {
        return "/api/error";
    }
}
