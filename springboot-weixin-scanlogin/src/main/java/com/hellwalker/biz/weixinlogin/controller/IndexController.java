package com.hellwalker.biz.weixinlogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {
    @GetMapping("/home")
    public String index() {
        return "index";
    }
}
