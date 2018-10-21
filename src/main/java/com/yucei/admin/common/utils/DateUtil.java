package com.yucei.admin.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author wyong
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/9/18
 */
@Slf4j
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {

    public final static String[] CHINESEWEEK = {"日", "一", "二", "三", "四", "五",
            "六"};

    final static String EAST_CHINES = "Asia/Shanghai";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYY_MM_DDHH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd
     */
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    ;


    /**
     * yyyyMMdd
     */
    public final static String YYYMMDD = "yyyyMMdd";


    /**
     * 将日期转换为字符串时间
     *
     * @param date       //日期
     * @param dateFormat //格式
     * @return
     */
    public static String date2string(Date date, DateTimeFormatter dateFormat) {
        return dateFormat.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(EAST_CHINES)));
    }


    public static Date string2date(String source, String format) {
        try {
            return DateUtils.parseDate(source, Locale.CHINESE, format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转化为字符串
     *
     * @param date   日期 如果为空，为当天
     * @param format 格式
     * @return String
     */
    public static String date2string(Date date, String format) {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern(format, Locale.CHINESE);
        if (date == null) {
            date = new Date();
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(EAST_CHINES)).format(sdf);

    }

    public static String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINESE));
    }

    /**
     * 要在高并发环境下能有比较好的体验，可以使用ThreadLocal来限制SimpleDateFormat只能在线程内共享，这样就避免了多线程导致的线程安全问题。
     */
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }


    public static void main(String[] args) throws ParseException {
       Date date = DateUtil.string2date("2018-10-18 14:37:00",DateUtil.YYYY_MM_DDHH_MM_SS);
       Date date2 = DateUtil.string2date("2018-10-18 14:36:00",DateUtil.YYYY_MM_DDHH_MM_SS);
       int second = (int) ((date.getTime() - date2.getTime()) / 1000);
        System.out.println(second);

    }

}
