package com.housoo.platform.supervise.util;

import com.housoo.platform.core.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 督办单编号生成工具类
 *
 * @author cjr
 * @date 2020-4-16
 */
public class SuperviseNoGenUtil {

    private static SuperviseNoGenUtil SuperviseNoGenUtil = null;

    /**
     * 取得SuperviseNoGenUtil的单例实现
     *
     * @return
     */
    public static SuperviseNoGenUtil getInstance() {
        if (SuperviseNoGenUtil == null) {
            synchronized (SuperviseNoGenUtil.class) {
                if (SuperviseNoGenUtil == null) {
                    SuperviseNoGenUtil = new SuperviseNoGenUtil();
                }
            }
        }
        return SuperviseNoGenUtil;
    }

    /**
     * 获取当前年月日
     */
    private static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    /**
     * 获取序列号
     *
     * @param currentNo
     * @return
     */
    public static synchronized String getNextNo(String currentNo) {
        String result = "";
        String ym = currentNo.substring(0, 8);
        String seq = currentNo.substring(8);
        //判断号码是不是当年当月的
        if (!getCurrentDate().equals(ym)) {
            //如果是新的一月的就直接变成001
            result = ym + "0001";
        } else {
            DecimalFormat df = new DecimalFormat("0000");
            //不是新的一月就累加
            if (StringUtils.isEmpty(seq)) {
                result = getCurrentDate() + "0001";
            } else {
                result = getCurrentDate() + df.format(counter(Integer.parseInt(seq)));
            }
        }
        return result;
    }

    /**
     * 累加
     *
     * @param count
     * @return
     */
    private static int counter(int count) {
        synchronized (UUIDGenerator.class) {
            if (count < 0) {
                count = 0;
            } else {
                count++;
            }
            return count;
        }
    }

}
