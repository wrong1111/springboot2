package com.yucei.admin.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 *
 * @author dazzlzy
 * @date 2018/4/26
 */
public class StringUtil {

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     *
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String lineToHump(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);

        str = sb.toString();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        return str;
    }

    /**
     * 驼峰转下划线,效率比上面高
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine(String)})
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String humpToLine2(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 首字母转小写
     *
     * @param s 字符串
     * @return 首字母小写的字符串
     */
    public static String toLowerCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s 字符串
     * @return 首字母大写的字符串
     */
    public static String toUpperCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuffer()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * object转String
     *
     * @param object object
     * @return String
     */
    public static String getString(Object object) {
        return getString(object, "");
    }

    /**
     * object转String，提供默认值
     *
     * @param object       object
     * @param defaultValue 默认值
     * @return String
     */
    public static String getString(Object object, String defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return object.toString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * object转int
     *
     * @param object object
     * @return int
     */
    public static int getInt(Object object) {
        return getInt(object, -1);
    }

    /**
     * object转int，提供默认值
     *
     * @param object       object
     * @param defaultValue 默认值
     * @return int
     */
    public static int getInt(Object object, Integer defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * object转Boolean
     *
     * @param object object
     * @return boolean
     */
    public static boolean getBoolean(Object object) {
        return getBoolean(object, false);
    }

    /**
     * object转boolean，提供默认值
     *
     * @param object       object
     * @param defaultValue 默认值
     * @return boolean
     */
    public static boolean getBoolean(Object object, Boolean defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 生成len 位随机整数
     * 最小1位，最大30位
     *
     * @return int
     * @throws
     * @Title: getSexRandom
     */
    public static String getRandom(int len) {
        if (len < 1) {
            len = 1;
        }
        if (len > 30) {
            len = 30;
        }
        double f = (Math.random() * 9 + 1);
        long p = pow2(BigInteger.valueOf(10), len - 1).longValue();
        BigDecimal big = BigDecimal.valueOf(f).multiply(BigDecimal.valueOf(p));
        return String.valueOf(big).replaceAll("[-|.]", "").substring(0, len);
    }

    ///##########################生成随机数长度。
    public static Random random = new Random();

    /**
     * 生成len 位随机整数
     * 包括数字字母
     *
     * @return int
     * @throws
     * @Title: getSexRandom
     */
    public static String getRandomStr(int len) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < len; i++) {
            boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
            if (isChar) { // 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                ret.append((char) (choice + random.nextInt(26)));
            } else { // 数字
                ret.append(Integer.toString(random.nextInt(10)));
            }
        }
        return ret.toString();
    }

    public static BigInteger pow2(BigInteger x, int n) {
        if (n == 0) {
            return BigInteger.valueOf(1);
        } else {
            if (n % 2 == 0) {
                return pow2(x.multiply(x), n / 2);
            } else {
                return pow2(x.multiply(x), (n - 1) / 2).multiply(x);
            }
        }

    }

    /**
     * 没有考虑并发问题。
     * @param type
     *       类型订单
     * @return
     */
    public static String getOrderNo(String type) {
        StringBuffer zero = new StringBuffer();
        StringBuffer orderno = new StringBuffer(18);
        Date date = new Date();
        String day = DateUtil.date2string(date,"yyyyMMdd");
        orderno.append(type).append(day).append(String.valueOf(System.currentTimeMillis()).substring(3));
        return orderno.toString();
    }
    public static void main(String[] args){
          System.out.println(getOrderNo("abc"));
    }
}
