package com.hellwalker.biz.weixinlogin.controller;

import com.hellwalker.biz.weixinlogin.config.WechatAccountConfig;
import com.hellwalker.common.exception.BizException;
import com.hellwalker.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
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

    /**
     * 【PC端】引导用户访问这个链接：
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
     * PC端登录，用户授权同意后回调的地址，从请求参数中获取code，state填充returnUrl
     * @param code 临时码
     * @param state returnUrl
     * @return
     */
    @GetMapping("/qr_login/callback")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String state) {
        //1. 通过code获取access_token
        WxOAuth2AccessToken wxOAuth2AccessToken = new WxOAuth2AccessToken();
        try {

            wxOAuth2AccessToken = wxOAuth2Service.getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("微信网页授权 【异常】 ---> " + e.getMessage());
            return "forward:/api/error";
        }

        log.info("微信登录 token info：" + wxOAuth2AccessToken);

        //从token中获取openid
        String openId = wxOAuth2AccessToken.getOpenId();

        // TODO: 进行业务逻辑操作，
        //  1. 查询数据库，查询third_oauth表和业务系统用户表，判断是否存在用户
        //  2. 如果不存在，重定向到客户端，提示用户输入手机号、验证码进行验证；否则不给予注册登录。
        //  3. 如果已经存在，更新数据库token，重定向到登录成功的页面；或者执行returnUrl；
        //  4. 提示：可以在微信的url中的state填充returnUrl

        log.info("openid={}", openId);
        if (!StringUtils.isEmpty(state)) {
            return "redirect:" + state;
        }

        // 2. 根据access token获取user info
        WxOAuth2UserInfo oAuth2UserInfo = new WxOAuth2UserInfo();
        try {
            oAuth2UserInfo = wxOAuth2Service.getUserInfo(wxOAuth2AccessToken, "cn");
        } catch (WxErrorException e) {
            log.error("【移动微信登录】获取 微信用户信息异常：" + e.getMessage());
        }
        String nickname = oAuth2UserInfo.getNickname();
        String headImgUrl = oAuth2UserInfo.getHeadImgUrl();


        // TODO: 业务逻辑操作
        //    查询数据库，更新或创建业务系统用户表信息

        // 默认回到主页
        return "index";
    }

    /**
     * 【移动端微信登录】请求，根据票据code获取access_token和user info。
     * 【该接口调用的时机】：移动端在客户端侧调用微信APP授权，用户成功授权后，移动端在微信提供的客户端工具的回调方法中，
     * 返回临时票据code，移动端应使用http client 使用get请求，调用该接口。参考：https://blog.51cto.com/u_15329836/3387031
     * @param code
     * @return
     */
    @GetMapping("/sns/mobile/access_token")
    @ResponseBody
    public CommonResult mobileQueryToken(@RequestParam("code") String code) {
        // 1. 获取微信access token
        WxOAuth2AccessToken wxOAuth2AccessToken = new WxOAuth2AccessToken();
        try {
            wxOAuth2AccessToken = wxOAuth2Service.getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【移动微信登录】获取access token异常：" + e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
        String openId = wxOAuth2AccessToken.getOpenId();
        String accessToken = wxOAuth2AccessToken.getAccessToken();
        String refreshToken = wxOAuth2AccessToken.getRefreshToken();
        int expiresIn = wxOAuth2AccessToken.getExpiresIn();

        // TODO: 进行业务逻辑操作，
        //  1. 查询数据库，查询third_oauth表和业务系统用户表，判断是否存在用户
        //  2. 如果不存在，重定向到客户端，提示用户输入手机号、验证码进行验证；否则不给予注册登录。
        //  3. 如果已经存在，更新数据库token.
        //  4. 生成登录jwt token，写入header，返回成功消息

        // 2. 根据access token获取user info
        WxOAuth2UserInfo oAuth2UserInfo = new WxOAuth2UserInfo();
        try {
            oAuth2UserInfo = wxOAuth2Service.getUserInfo(wxOAuth2AccessToken, "cn");
        } catch (WxErrorException e) {
            log.error("【移动微信登录】获取 微信用户信息异常：" + e.getMessage());
        }
        String nickname = oAuth2UserInfo.getNickname();
        String headImgUrl = oAuth2UserInfo.getHeadImgUrl();


        // TODO: 业务逻辑操作
        //    查询数据库，更新或创建业务系统用户表信息


        return CommonResult.success(openId);
    }
}
