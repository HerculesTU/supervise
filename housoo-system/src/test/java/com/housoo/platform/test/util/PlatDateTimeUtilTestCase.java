package com.housoo.platform.test.util;

import java.util.Date;

import com.housoo.platform.core.util.PlatDateTimeUtil;

/**
 * @author 胡裕
 *
 * 
 */
public class PlatDateTimeUtilTestCase {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Date date = new Date();
        int hour = PlatDateTimeUtil.getHour(date);
        System.out.println(PlatDateTimeUtil.getTimeSection(hour));
    }

}
