package com.yucei.admin.common.utils;

/**
 * @author wyong
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/9/18
 */
public class ValidateUtil {

    public static boolean isMobilephone(String mobile) {
        return mobile.matches("^1\\d{10}$");
    }

    public static boolean isCode(String code) {
        return true;
    }

    public static boolean isPicCode(String piccode) {
        return true;
    }

    public static boolean isSimplePassword(String simplepassword) {
        return false;
    }
}
