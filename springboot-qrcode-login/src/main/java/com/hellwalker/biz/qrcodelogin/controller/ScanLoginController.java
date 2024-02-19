package com.hellwalker.biz.qrcodelogin.controller;

import com.hellwalker.biz.qrcodelogin.service.ScanService;
import com.hellwalker.common.exception.BizException;
import com.hellwalker.common.result.CommonResult;
import com.hellwalker.common.utils.QRCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 扫码登录控制器
 */
@Controller
@RequestMapping("/api/qrcode")
public class ScanLoginController {
    @Autowired
    private ScanService scanService;

    /**
     * 生成二维码
     * @return
     */
    @GetMapping("/generate")
    @ResponseBody
    public void generateQrCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CommonResult commonResult = scanService.generateUUID();
        System.out.println("UUID: " + commonResult.getData());
        if (commonResult.getCode() == 10000) {

            QRCodeUtil.getQRCode((String) commonResult.getData(), response.getOutputStream());
        }
        throw new BizException(commonResult.getMsg());
    }

    /**
     * 轮询二维码状态
     * @param qrcodeId
     * @return
     */
    @GetMapping("/query/qrcode_status")
    @ResponseBody
    public CommonResult queryQRCodeStatus(@RequestParam("qrcode_id") String qrcodeId) {
        return scanService.queryInfoUUID(qrcodeId);
    }

    /**
     * 扫码登录
     * @param uuid 二维码uuid
     * @param mobileToken 已登录移动设备端token
     * @param device 设备信息
     * @return
     */
    @GetMapping("/scan_qrcode_login")
    @ResponseBody
    public CommonResult scanQrLogin(@RequestParam("uuid") String uuid,
                                    @RequestParam("mobile_token") String mobileToken,
                                    @RequestParam("device") String device) {
        CommonResult commonResult = scanService.scanQrLogin(uuid, mobileToken, device);
        return commonResult;
    }


    /**
     * 确认登录，返回用户token以及对应信息
     * @param uuid 二维码uuid
     * @param onceToken 一次性token
     * @param mobileToken  移动端已登录token
     * @param device 设备信息
     * @return
     */
    @GetMapping("/confirm_qr_login")
    @ResponseBody
    public CommonResult confirmQrLogin(@RequestParam("uuid") String uuid,
                                       @RequestParam("once_token") String onceToken,
                                       @RequestParam("mobile_token") String mobileToken,
                                       @RequestParam("device") String device) {
        if (StringUtils.isAnyEmpty(uuid, onceToken, mobileToken, device)) {
            return CommonResult.failed("参数缺失 || 不完整");
        }
        CommonResult commonResult = scanService.confirmQrLogin(uuid, onceToken, mobileToken, device);
        return commonResult;
    }
}
