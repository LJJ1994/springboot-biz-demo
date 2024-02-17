package com.hellwalker.biz.qrcodelogin.util;


import com.hellwalker.biz.qrcodelogin.model.CodeStatus;
import com.hellwalker.biz.qrcodelogin.model.CodeVO;

/**
 * 二维码工具类
 */
public class BizCodeUtil {

    /**
     * 获取过期二维码存储信息
     *
     * @return 二维码值对象
     */
    public static CodeVO getExpireCodeInfo() {
        return new CodeVO(CodeStatus.EXPIRE,"二维码已更新");
    }

    /**
     * 获取未使用二维码存储信息
     *
     * @return 二维码值对象
     */
    public static CodeVO getUnusedCodeInfo() {
        return new CodeVO(CodeStatus.UNUSED,"二维码等待扫描");
    }

    /**
     * 获取已扫码二维码存储信息
     */
    public static CodeVO getConfirmingCodeInfo(String onceToken, String device) {
        return new CodeVO(CodeStatus.CONFIRMING,"二维码扫描成功，等待确认", onceToken, device);
    }

    /**
     * 获取已扫码确认二维码存储信息
     * @return 二维码值对象
     */
    public static CodeVO getConfirmedCodeInfo(String token) {
        return new CodeVO(CodeStatus.CONFIRMED, "二维码已确认",token);
    }

}
