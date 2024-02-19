package com.hellwalker.biz.weixinlogin.constant;

public enum LoginType {
    WEIXIN_LOGIN(1, "微信扫码登录"),
    QQ_LOGIN(2, "QQ登录"),
    WEIBO_LOGIN(3, "微博登录"),
    GITEE(4, "Gitee登录")
    ;
    private final int loginType;

    private final String desc;

    LoginType(int loginType, String desc) {
        this.loginType = loginType;
        this.desc = desc;
    }

    public int getLoginType() {
        return loginType;
    }

    public String getDesc() {
        return desc;
    }
}
