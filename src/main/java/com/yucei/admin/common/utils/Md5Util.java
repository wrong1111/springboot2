package com.yucei.admin.common.utils;

/**
 * @author wyong
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/10/9
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }

    // 32位
    public static String md5_32(String text) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }

        try {
            msgDigest.update(text.getBytes("utf-8")); // 注意改接口是按照指定编码形式签名

        } catch (UnsupportedEncodingException e) {

            throw new IllegalStateException(
                    "System doesn't support your  EncodingException.");
        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    // encodeHex
    public static char[] encodeHex(byte[] data) {
        int l = data.length;

        char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

    // 16位
    public static String md5_16(String text) {
        String str_32 = md5_32(text);
        return str_32.substring(8, 24);
    }


    /* 小写的MD5 */
    public static String MD5lower(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptMD5(String str, String salt, String charset) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update((str + salt).getBytes(charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byte2hex(md5.digest());
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }

        return sign.toString();
    }

    // main
    public static void main(String[] args) {
        String code_img_url= "http://www.crowdfun.cn/pay/qrcode?uuid=http%3A%2F%2Ft.cn%2FR34dogi";
        String code_url="http://t.cn/R34dogi";
        String out_trade_no="201805116009226613";
        String key="7Pubaeeel7RWTusLGTqMtIVf48P8He9n";
        String a =  "code_img_url="+code_img_url+"code_url="+code_url+"out_trade_no="+out_trade_no;
        String md5 = Md5Util.encryptMD5(a,key,"UTF-8");
        System.out.println(md5+"=>9930B8BA4801B8654EDB361099E7C9C3");
    }
}
