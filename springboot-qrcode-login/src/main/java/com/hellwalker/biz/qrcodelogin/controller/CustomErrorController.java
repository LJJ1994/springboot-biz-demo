package com.hellwalker.biz.qrcodelogin.controller;

import com.hellwalker.biz.qrcodelogin.model.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/api/error")
    @ResponseBody
    public CommonResult handleError(WebRequest webRequest, Model model) {
//        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest,
//                ErrorAttributeOptions.of(Include.STACK_TRACE, Include.MESSAGE, Include.EXCEPTION, Mode.MESSAGE, Output.JSON));
//
//        model.addAttribute("status", errorAttributes.get("status"));
//        model.addAttribute("error", errorAttributes.get("error"));
//        model.addAttribute("message", errorAttributes.get("message"));
//        model.addAttribute("timestamp", errorAttributes.get("timestamp"));
//        model.addAttribute("path", errorAttributes.get("path"));
//
//        // You can add additional attributes as needed
//
//        return "error"; // Provide the name of your custom error view
        return CommonResult.failed("系统错误");
    }

    @Override
    public String getErrorPath() {
        return "/api/error";
    }
}
