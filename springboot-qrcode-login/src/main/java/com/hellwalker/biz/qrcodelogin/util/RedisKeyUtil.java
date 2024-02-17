package com.hellwalker.biz.qrcodelogin.util;

public class RedisKeyUtil {
    private static final long SCAN_LOGIN_KEY_TIME_OUT = 60; // 扫码登录二维码过期时间

    public static final long LOGIN_TOKEN_TIME_OUT = 60; // 登录token过期时间

    /**
     * 扫码登录确认中，用户id和二维码信息关联
     * 参数为用户id，值为二维码CodeVO
     */
    public static final String CONFIRMING_KEY = "qrcode:login:confirming:%s";

    private static final String SCAN_LOGIN_KEY_PREFIX = "scan:login:%s"; // %s为uuid

    /**
     * 根据登录成功生成的token，获取用户id
     * 第一个参数为设备信息
     * 第二个参数为token
     */
    public static final String LOGIN_TOKEN_KEY = "login:token:%s:%s";

    /**
     * 根据用户id，获取登录成功的token
     * 第一个参数为设备信息
     * 第二个参数为userid
     */
    public static final String LOGIN_USERID_KEY = "login:userid:%s:%s";

    /**
     * 扫码登录二维码信息----redis中的过期时间
     * 默认为一分钟
     * @return
     */
    public static long getTimeOut() {
        return SCAN_LOGIN_KEY_TIME_OUT;
    }

    /**
     * 获取扫码登录 redis中的键
     * 格式：scan:login:%s, %s为uuid
     * @param uuid
     * @return
     */
    public static String getScanUUIDKey(String uuid) {
        String scanLoginKey = String.format(SCAN_LOGIN_KEY_PREFIX, uuid);
        return scanLoginKey;
    }


}
