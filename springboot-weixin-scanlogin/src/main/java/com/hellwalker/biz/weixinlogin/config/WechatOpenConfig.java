package com.hellwalker.biz.weixinlogin.config;

import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpStoreService;
import me.chanjar.weixin.mp.api.impl.WxMpOAuth2ServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatOpenConfig {

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxOpenService = new WxMpServiceImpl();
        wxOpenService.setWxMpConfigStorage(wxOpenConfigStorage());
        return wxOpenService;
    }

    @Bean
    public WxMpConfigStorage wxOpenConfigStorage() {

        WxMpDefaultConfigImpl wxMpInMemoryConfigStorage = new WxMpDefaultConfigImpl();
        wxMpInMemoryConfigStorage.setAppId(accountConfig.getOpenAppId());
        wxMpInMemoryConfigStorage.setSecret(accountConfig.getOpenAppSecret());
        wxMpInMemoryConfigStorage.setToken(accountConfig.getToken());
        return wxMpInMemoryConfigStorage;
    }

    @Bean
    public WxOAuth2Service wxOAuth2Service() {
        return new WxMpOAuth2ServiceImpl(wxMpService());
    }
}
