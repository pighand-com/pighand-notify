package com.pighand.notify.util;

import java.net.InetAddress;

/**
 * 当前机器信息
 *
 * @author wangshuli
 */
public class MachineUtil {

    /**
     * 获取机器名
     *
     * <p>ip 或 name。ip是127.0.0.1取name。获取失败返回时间戳
     *
     * @return
     */
    public static String getName() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String name = ia.getHostName();
            String ip = ia.getHostAddress();

            return ip.equals("127.0.0.1") ? name : ip;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(System.currentTimeMillis());
    }
}
