package com.hellwalker.biz.weixinlogin;

import com.hellwalker.biz.weixinlogin.config.WechatAccountConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatConfigTest {
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Test
    public void getWechatAccountConfigTest() {
        System.out.println(wechatAccountConfig.toString());
    }

}
