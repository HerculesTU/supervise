package com.housoo.platform.core.util;

import java.text.DecimalFormat;

/**
 * @author 数字工具类
 */
public class PlatNumberUtil {
    /**
     * 获取四舍五入后的整型值
     *
     * @param targetValue
     * @return
     */
    public static int getRoundingValue(Double targetValue) {
        DecimalFormat df = new DecimalFormat("#");
        String result = df.format(targetValue);
        return Integer.parseInt(result);
    }

    /**
     * 获取四舍五入后的
     *
     * @param targetValue
     * @param decimalCount 小数点位数
     * @return
     */
    public static String getRoundingValue(Double targetValue, int decimalCount) {
        String format = "#.";
        for (int i = 0; i < decimalCount; i++) {
            format += "0";
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(targetValue);
    }
}
