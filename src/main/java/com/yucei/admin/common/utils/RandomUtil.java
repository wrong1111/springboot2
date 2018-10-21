package com.yucei.admin.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author dazzlzy
 * @date 2018/5/19
 */
@Slf4j
public class RandomUtil {
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERCHAR = "0123456789";

    /**
     * 获取默认位数（8位）的盐
     *
     * @return 8位的随机盐
     */
    public static String getSalt() {
        return getSalt(12);
    }

    /**
     * 获取随机盐
     * 使用SecureRandom安全的伪随机数
     *
     * @param size 盐的位数
     * @return 随机盐
     */
    public static String getSalt(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    /**
     * 获取UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取不带 `-` 的UUID
     *
     * @return UUID
     */
    public static String getShortUUID() {
        return getUUID().replaceAll("-", "");
    }




    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length 字符串长度
     * @return 纯0字符串
     */
    public static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 每次生成的len位数都不相同
     *
     * @param len 默认 4位
     * @return 定长的数字
     */
    public static String getNotSimple( int len) {
        StringBuilder simple = new StringBuilder();
        int[] param = new int[]{0,1,2,3,4,5,6,7,8,9};
        Random rand = new Random();
        for (int i = len; i > 0; i--) {
            int index = rand.nextInt(i)%10;
            simple.append(param[index]);
        }
        return simple.toString();
    }

    public static void main(String[] args) {
        System.out.println("返回一个定长的随机字符串(只包含大小写字母、数字):" + generateString(10));
        System.out
                .println("返回一个定长的随机纯字母字符串(只包含大小写字母):" + generateMixString(10));
        System.out.println("返回一个定长的随机纯大写字母字符串(只包含大小写字母):"
                + generateLowerString(10));
        System.out.println("返回一个定长的随机纯小写字母字符串(只包含大小写字母):"
                + generateUpperString(10));
        System.out.println("生成一个定长的纯0字符串:" + generateZeroString(10));
        System.out.println("根据数字生成一个定长的字符串，长度不够前面补0:"
                + toFixdLengthString(123, 10));
        System.out.println("每次生成的len位数都不相同:" + getNotSimple(15));
    }

}
