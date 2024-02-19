package com.hellwalker.biz.weixinlogin.controller;

import com.hellwalker.biz.weixinlogin.constant.LoginType;
import com.hellwalker.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录控制器
 */
@Controller
@RequestMapping("/api/login")
public class ApiLoginController {
    /**
     * 第三方登录
     * @param loginType 登录类型
     * @param oauthType oauth动作
     * @return
     */
    @GetMapping("/sns")
    public String snsLogin(@RequestParam("loginType") Integer loginType,
                           @RequestParam("oauthType") String oauthType) {
        if (loginType == null || StringUtils.isEmpty(oauthType)) {
            throw new BizException("参数不完整");
        }

        if (LoginType.WEIXIN_LOGIN.getLoginType() == loginType) {
            return "forward:/wechat/qrAuthorize";
        }

        return "forward:/api/error";
    }
}
