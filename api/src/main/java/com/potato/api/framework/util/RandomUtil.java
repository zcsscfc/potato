package com.potato.api.framework.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 随机数工具类
 *
 * @author huangyong
 * @since 1.0.0
 */
public final class RandomUtil {

    private RandomUtil() {
    }

    /**
     * 生成随机数
     */
    public static String getRandom(int count) {
        return RandomStringUtils.randomNumeric(count);
    }
}
