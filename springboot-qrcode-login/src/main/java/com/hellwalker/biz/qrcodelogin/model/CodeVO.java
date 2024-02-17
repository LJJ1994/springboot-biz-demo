package com.hellwalker.biz.qrcodelogin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 二维码扫码登录---POJO类
 * @param <T>
 */
@Data
@NoArgsConstructor
public class CodeVO<T> {

    /**
     * 二维码状态
     */
    private CodeStatus codeStatus;

    /**
     * 提示消息
     */
    private String message;

    /**
     * token
     */
    private T token;

    /**
     * 设备号
     */
    private String device;

    public CodeVO(CodeStatus codeStatus) {
        this.codeStatus = codeStatus;
    }

    public CodeVO(CodeStatus codeStatus,String message) {
        this.codeStatus = codeStatus;
        this.message = message;
    }

    public CodeVO(CodeStatus codeStatus,String message,T token) {
        this.codeStatus = codeStatus;
        this.message = message;
        this.token=token;
    }

    public CodeVO(CodeStatus codeStatus, String message, T token, String device) {
        this.codeStatus = codeStatus;
        this.message = message;
        this.token = token;
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeVO<?> codeVO = (CodeVO<?>) o;
        return codeStatus == codeVO.codeStatus && Objects.equals(message, codeVO.message) && Objects.equals(token, codeVO.token) && Objects.equals(device, codeVO.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeStatus, message, token, device);
    }

    public static void main(String[] args) {
        CodeVO<Object> v1 = new CodeVO<>();
        v1.setCodeStatus(CodeStatus.CONFIRMED);
        v1.setDevice("a1");
        v1.setMessage("success");
        v1.setToken("123");

        CodeVO<Object> v2 = new CodeVO<>();
        v2.setCodeStatus(CodeStatus.CONFIRMED);
        v2.setDevice("a1");
        v2.setMessage("success");
        v2.setToken("123");

        System.out.println(v1.equals(v2));
    }
}

