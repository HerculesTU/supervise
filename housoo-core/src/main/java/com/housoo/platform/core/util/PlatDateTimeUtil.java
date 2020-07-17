package com.housoo.platform.core.util;

import jodd.datetime.JDateTime;
import org.joda.time.Hours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 描述 平台封装时间日期的工具类
 *
 * @author
 * @created 2017年1月6日 下午5:31:08
 */
public class PlatDateTimeUtil {

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化字符串为时间
     *
     * @param time
     * @param format
     * @return
     */
    public static Date formatStr(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式字符串
     *
     * @param time
     * @param oldFormat 旧格式
     * @param newFormat 新格式
     * @return
     */
    public static String formatStr(String time, String oldFormat, String newFormat) {
        Date d = PlatDateTimeUtil.formatStr(time, oldFormat);
        return formatDate(d, newFormat);
    }

    /**
     * 将字符串转换成日期类型
     *
     * @param time
     * @return
     */
    public static Date conventStringToDate(String time) {
        JDateTime jdt = new JDateTime(time);
        return jdt.convertToDate();
    }

    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        JDateTime jdt = new JDateTime();
        jdt.setDateTime(date);
        return jdt.getYear();
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        JDateTime jdt = new JDateTime();
        return jdt.getYear();
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        JDateTime jdt = new JDateTime();
        jdt.setDateTime(date);
        return jdt.getMonth();
    }

    /**
     * 获取日期
     *
     * @param date
     * @return
     */
    public static int getDate(Date date) {
        JDateTime jdt = new JDateTime();
        jdt.setDateTime(date);
        return jdt.getDay();
    }

    /**
     * 获取小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        JDateTime jdt = new JDateTime();
        jdt.setDateTime(date);
        return jdt.getHour();
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        JDateTime jdt = new JDateTime();
        return jdt.getMonth();
    }

    /**
     * 获取当前年的周数
     *
     * @return
     */
    public static int getCurrentWeekOfYear() {
        JDateTime jdt = new JDateTime();
        return jdt.getWeekOfYear();
    }

    /**
     * 获取两个日期之间间隔的天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(String beginDate, String endDate) {
        Date bDate = conventStringToDate(beginDate);
        Date eDate = conventStringToDate(endDate);
        Calendar d1 = new GregorianCalendar();
        Calendar d2 = new GregorianCalendar();
        if (bDate.before(eDate)) {
            d1.setTime(bDate);
            d2.setTime(eDate);
        } else {
            d1.setTime(eDate);
            d2.setTime(bDate);
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        if (bDate.after(eDate)) {
            days = -days;
        }
        return days;
    }

    /**
     * 取得两个时间段的间隔月数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getMonthsBetween(String beginDate, String endDate) {
        Date bDate = conventStringToDate(beginDate);
        Date eDate = conventStringToDate(endDate);
        Calendar d1 = new GregorianCalendar();
        Calendar d2 = new GregorianCalendar();
        if (bDate.before(eDate)) {
            d1.setTime(bDate);
            d2.setTime(eDate);
        } else {
            d1.setTime(eDate);
            d2.setTime(bDate);
        }
        int months = d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                months += 12;
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }

        if (bDate.after(eDate)) {
            months = -months;
        }
        return months;
    }

    /**
     * 取得两个时间段的间隔年数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getYearsBetween(String beginDate, String endDate) {
        Date bDate = conventStringToDate(beginDate);
        Date eDate = conventStringToDate(endDate);
        Calendar d1 = new GregorianCalendar();
        Calendar d2 = new GregorianCalendar();
        if (bDate.before(eDate)) {
            d1.setTime(bDate);
            d2.setTime(eDate);
        } else {
            d1.setTime(eDate);
            d2.setTime(bDate);
        }
        int years = d2.get(Calendar.YEAR) - d1.get(Calendar.YEAR);
        if (bDate.after(eDate)) {
            years = -years;
        }
        return years;
    }

    /**
     * 描述 计算延后的时间
     *
     * @param nowDate:目标时间
     * @param field:延后时间的字段,例如Calendar.DATE
     * @param delay:延后的数值
     * @return
     * @created 2016年6月18日 下午2:18:26
     */
    public static Date getNextTime(Date nowDate, int field, int delay) {
        Calendar c = new GregorianCalendar();
        c.setTime(nowDate);
        c.add(field, delay);
        return c.getTime();
    }

    /**
     * 描述 获取间隔时间
     *
     * @param beginTime
     * @param endTime
     * @param timeFormat
     * @param type       1:返回间隔秒  2:返回间隔小时 3:返回间隔分钟 4:返回间隔天数
     * @return
     * @created 2016年3月8日 下午3:53:34
     */
    public static long getIntervalTime(String beginTime, String endTime, String timeFormat, int type) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = sdf.parse(beginTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        // 默认为毫秒，除以1000是为了转换成秒
        long interval = (endDate.getTime() - beginDate.getTime()) / 1000;// 秒
        switch (type) {
            case 1:
                return interval % 60;
            case 2:
                return interval / (60 * 60);
            case 3:
                return interval / 60;
            case 4:
                return interval / (24 * 3600);
            default:
                break;
        }
        return interval;
    }

    /**
     * 将日期转换成unix时间戳
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static String dateToTimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据小时获取时间段
     *
     * @param hour
     * @return 1:上午 2:下午 3:晚上
     */
    public static int getTimeSection(int hour) {
        if (hour >= 6 && hour <= 13) {
            return 1;
        } else if (hour >= 14 && hour <= 18) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 格式化时间
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static String formatGMTDate(String dateStr, String format) {
        //字符串转Date
        if (dateStr.contains("CST")) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM ddHH:mm:ss 'CST' yyyy", Locale.US);
            Date date = null;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formatDate(date, format);
        } else {
            return formatDate(formatStr(dateStr, format), format);
        }

    }

    public static String formatCSTDate(String cstDateStr, String format) {
        Date date = null;
        SimpleDateFormat sdf1 = null;
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        try {
            sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date = sdf1.parse(cstDateStr);
        } catch (ParseException e) {
            sdf1 = new SimpleDateFormat("yyyy'年' MM'月' dd'日' EEE HH:mm:ss Z", Locale.CHINA);
            try {
                date = sdf1.parse(cstDateStr);
            } catch (ParseException e1) {
                System.out.println("CST日期格式转换出错，原因：" + e.getMessage());
                return null;
            }
        }

        return sdf2.format(date);
    }

    /**
     * 时间戳转换成日期格式字符串
     * cjr
     *
     * @param seconds 精确到秒的字符串
     * @param format  日期格式
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public static void main(String[] args) {
        System.out.println(timeStamp2Date("1589008696265", "yyyy-MM-dd HH:mm:ss"));
    }

}
