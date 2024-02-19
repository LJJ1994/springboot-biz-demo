package com.hellwalker.biz.qrcodelogin;

import com.hellwalker.biz.qrcodelogin.config.DeviceProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevicePropertiesTest {
    @Test
    public void getDeviceTest() {
        List<String> device = DeviceProperties.getDevice();
        System.out.println(device);
    }
}
