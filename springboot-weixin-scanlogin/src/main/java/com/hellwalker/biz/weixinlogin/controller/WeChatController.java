package com.hellwalker.biz.weixinlogin.controller;

import com.hellwalker.biz.weixinlogin.config.WechatAccountConfig;
import com.hellwalker.common.exception.BizException;
import com.hellwalker.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxOAuth2Service wxOAuth2Service;

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    /*
     * @param signature 微信加密签名，signature结合了开发者填写的 token 参数和请求中的 timestamp 参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     这是个随机数
     * @param echostr   随机字符串，验证成功后原样返回
     */
    @GetMapping
    public void get(@RequestParam(required = false) String signature,
                    @RequestParam(required = false) String timestamp,
                    @RequestParam(required = false) String nonce,
                    @RequestParam(required = false) String echostr,
                    HttpServletResponse response) throws IOException {
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            log.warn("接收到了未通过校验的微信消息，这可能是token配置错了，或是接收了非微信官方的请求");
            return;
        }
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(echostr);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 引导用户访问这个链接：
     * 1、这个链接是网页端，会生成二维码。
     * 2、用户使用微信扫码，进行授权。
     * @return
     */
    @GetMapping("/qrAuthorize")
    public String qrAuthorize() {
        //returnUrl就是用户授权同意后回调的地址
        String returnUrl = wechatAccountConfig.getLoginRedirectUri();

        //引导用户访问这个链接，进行授权
        String authorizationUrl = wxMpService.buildQrConnectUrl(returnUrl,
                WxConsts.QrConnectScope.SNSAPI_LOGIN,
                URLEncoder.encode(returnUrl));
        return "redirect:" + authorizationUrl;
    }

    /**
     * 用户授权同意后回调的地址，从请求参数中获取code，state填充returnUrl
     * @param code 临时码
     * @param state returnUrl
     * @return
     */
    @GetMapping("/qr_login/callback")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String state) {
        WxOAuth2AccessToken wxMpOAuth2AccessToken = new WxOAuth2AccessToken();
        try {
            //通过code获取access_token
            wxMpOAuth2AccessToken = wxOAuth2Service.getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("微信网页授权 【异常】 ---> " + e.getMessage());
            return "forward:/api/error";
        }

        log.info("微信登录 token info：" + wxMpOAuth2AccessToken);

        //从token中获取openid
        String openId = wxMpOAuth2AccessToken.getOpenId();

        // TODO: 进行业务逻辑操作，
        //  1. 查询数据库，查询third_oauth表，判断是否存在用户
        //  2. 如果不存在，重定向到客户端，提示用户输入手机号、验证码进行验证；否则不给予注册登录。
        //  3. 如果已经存在，更新数据库token，重定向到登录成功的页面；或者执行returnUrl；
        //  4. 提示：可以在微信的url中的state填充returnUrl

        log.info("openid={}", openId);
        if (!StringUtils.isEmpty(state)) {
            return "redirect:" + state;
        }

        // 默认回到主页
        return "index";
    }
}
