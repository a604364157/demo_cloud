package com.jjx.cloudplus.utils;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author jiangjx
 */
public class Base64Util {

    public static String base64Decode(String base64) {
        return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    }

    public static String base64Encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }


    public static void main(String[] args) {
        String a = "我的天啦文件传输助手有毒";
        String b = base64Encode(a);
        System.out.println(b);
        System.out.println(base64Decode(b));
    }

}
