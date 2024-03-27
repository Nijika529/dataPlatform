package com.cqie.util.uuid;

import java.util.UUID;

/**
 * UUID工具类，提供生成UUID的方法。
 */
/**
 * 全局统一返回结果类
 * @author cx
 */

public class UUIDUtils {

    /**
     * 生成随机UUID（版本4）。
     * @return 随机生成的UUID字符串。
     */
    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成基于时间和硬件地址的UUID（版本1）。
     * 注意：这种类型的UUID可能会重复，但在全局范围内概率非常低。
     * @return 基于时间和硬件地址生成的UUID字符串。
     */
    public static String generateTimeBasedUuid() {
        UUID uuid = new UUID(System.currentTimeMillis(), System.nanoTime());
        return uuid.toString();
    }

    /**
     * 生成不带横线的UUID。
     * @return 不带横线的UUID字符串。
     */
    public static String generateWithoutDashes() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}