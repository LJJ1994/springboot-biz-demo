package com.hellwalker.biz.qrcodelogin.util;

import java.util.UUID;

public final class UUIDUtils {
    public static String generateUUID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replace("-", "");
    }
}
