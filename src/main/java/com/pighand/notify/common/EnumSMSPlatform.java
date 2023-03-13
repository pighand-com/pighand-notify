package com.pighand.notify.common;

/**
 * 短信发送平台
 *
 * @author wangshuli
 */
public enum EnumSMSPlatform {
    /** 腾讯云 */
    TENCENT(1),

    /** 阿里云 */
    ALIYUN(2);

    public final int value;

    EnumSMSPlatform(int value) {
        this.value = value;
    }

    public static EnumSMSPlatform get(int value) {
        return switch (value) {
            case 2 -> ALIYUN;
            default -> TENCENT;
        };
    }
}
