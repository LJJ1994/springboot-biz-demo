package com.hellwalker.biz.qrcodelogin.model;

public enum CodeStatus {

    /**
     * 过期
     */
    EXPIRE,

    /**
     * 未使用的二维码
     */
    UNUSED,

    /**
     * 已扫码, 等待确认
     */
    CONFIRMING,

    /**
     * 确认登录成功
     */
    CONFIRMED

}

