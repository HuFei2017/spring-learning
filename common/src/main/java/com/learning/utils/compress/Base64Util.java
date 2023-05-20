package com.learning.utils.compress;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    /**
     * @Description 文本Base64加密
     * @Param [str] 加密前内容
     * @Return java.lang.String 加密后内容
     * @Author hufei
     * @Date 2020/8/19 11:17
     */
    public static String compress(String str) {
        return Base64.getUrlEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8)).replace('-', '+').replace('_', '/');
    }

    /**
     * @Description 文本Base64解密
     * @Param [base64Str] 解密前内容
     * @Return java.lang.String 解密后内容
     * @Author hufei
     * @Date 2020/8/19 11:17
     */
    public static String uncompress(String base64Str) {
        return new String(Base64.getDecoder().decode(base64Str.replaceAll("[^A-Za-z0-9+/=]", "+")), StandardCharsets.UTF_8);
    }
}
