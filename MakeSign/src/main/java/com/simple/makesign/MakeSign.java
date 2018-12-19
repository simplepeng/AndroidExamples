package com.simple.makesign;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MakeSign {

    public static Map<String,String> makeSign(String json) {
        TreeMap<String,String> resultMap = new TreeMap<>();
        String app_secret = "17378a5fe415fc1abf05b25f606392d7";
        TreeMap<String, String> map = new Gson()
                .fromJson(json, new TypeToken<TreeMap<String, String>>() {
                }.getType());
        String urlParams = null;
        try {
            urlParams = toUrlParams(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("urlParams",urlParams);
        String upperCase = urlParams.toUpperCase();
        resultMap.put("upperCase",upperCase);
        String md5 = MD5Utils.getMD5String(upperCase).toLowerCase();
        resultMap.put("md5",md5);
        String sign = hash("SHA-256", md5 + app_secret);
        resultMap.put("sign",sign);
        return resultMap;
    }

    public static String toUrlParams(Map<String, String> map) throws Exception {
        String buff = "";
        String[] change_array = {"address", "content", "nickname",
                "oss_img", "avatar", "back_img", "font_color"};

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (in_array(key, change_array)) {
                buff = buff + key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
            } else {
                buff = buff + key + "=" + value + "&";
            }

        }
        return buff.substring(0, buff.length() - 1);
    }

    public static boolean in_array(String key, String[] array) {
        List<String> list = Arrays.asList(array);
        return list.contains(key);
//        boolean in = false;
//        for (String s : array) {
//            in = key.equals(s);
//        }
//        return in;
    }

    private static String md5(String str) {
        // 生成一个MD5加密计算摘要
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 计算md5函数
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    private static String hash(final String strType, final String strText) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }
}
