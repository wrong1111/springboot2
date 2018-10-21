package com.yucei.admin.common.utils;

import org.apache.commons.codec.digest.Md5Crypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码工具类
 *
 * @author zhaozhenyao
 * @date 2018/5/13
 */
public class PasswordUtil {


    /**
     * 加密密码
     *
     * @param password 明文密码
     * @param salt     盐 $1$aa
     * @return 密文密码
     */
    public static String encrypt(String password, String salt) {
        try {
            return Md5Crypt.md5Crypt(password.getBytes("utf-8"), salt);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args){
        String salt = "$1$yucei";
       System.out.println(PasswordUtil.encrypt("yuceiabc",salt));

    }

}
