package com.hellwalker.biz.qrcodelogin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 正式 token
     */
    private T token;

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

}

