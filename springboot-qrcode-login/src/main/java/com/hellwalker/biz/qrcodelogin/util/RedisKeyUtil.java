package com.hellwalker.biz.qrcodelogin.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

public class RedisKeyUtil {
    private static final long SCAN_LOGIN_KEY_TIME_OUT = 1; // minute

    private static final String SCAN_LOGIN_KEY_PREFIX = "scan:login:%s"; // %s为uuid

    public static long getTimeOut() {
        return SCAN_LOGIN_KEY_TIME_OUT;
    }

    public static String getScanUUID(String uuid) {
        String scanLoginKey = String.format(SCAN_LOGIN_KEY_PREFIX, uuid);
        return scanLoginKey;
    }


}
