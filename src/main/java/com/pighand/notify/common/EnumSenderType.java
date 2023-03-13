package com.pighand.notify.common;

/**
 * 发送类型
 *
 * @author wangshuli
 */
public enum EnumSenderType {

    /** 邮箱发送 */
    EMAIL(1);

    public final int value;

    EnumSenderType(int value) {
        this.value = value;
    }

    public static EnumSenderType get(int value) {
        return switch (value) {
            case 1 -> EMAIL;
            default -> EMAIL;
        };
    }
}
