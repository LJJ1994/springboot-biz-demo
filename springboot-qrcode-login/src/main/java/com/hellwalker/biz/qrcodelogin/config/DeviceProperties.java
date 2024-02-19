package com.hellwalker.biz.qrcodelogin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class DeviceProperties {
    private static final Logger logger = LoggerFactory.getLogger(DeviceProperties.class);

    private static List<String> device;

    public static List<String> getDevice() {
        return device;
    }

    @PostConstruct
    public void init() {
        // 在应用启动时加载配置文件
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DeviceProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            // 获取配置文件中的 my.device 属性值
            String deviceProperty = properties.getProperty("my.device");

            // 将属性值解析为列表
            if (deviceProperty != null && !deviceProperty.isEmpty()) {
                String[] split = deviceProperty.split(",");
                device = Arrays.stream(split).collect(Collectors.toList());
            }

        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            logger.error("DeviceProperties加载异常：" + e.getMessage());

        }
    }
}

