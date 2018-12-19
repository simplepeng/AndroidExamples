package com.simple.makesign;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        System.out.println("start-----------");
        Map<String, String> map = new HashMap<>();
//        map.put("appid", "wxd99ea890f98497ca");
//        map.put("noncestr", "85tlyycpr81NtG1l9Bt6RKPDlCj6YDab");
//        map.put("timestamp", "1541670418269");
//        map.put("avatarUrl", "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJ7oLP4uNnV9vAOrytfBCluGicM5lruqngLicOIibyIGxlVCqlQA6NY77M8sPzBGRuib2BJicTZUxQu5VQ/132");
//        map.put("openid", "owewm0W8GYDgdYJ-ySf35pO7cbGk");
//        map.put("city", "重庆");
//        map.put("country", "China");
//        map.put("gender", "1");
//        map.put("nickName", "SleepingTriumph");
//        map.put("province", "Chongqing");
//        map.put("address", "%E5%85%89%E7%94%B5%E5%9B%AD%E5%8F%8C%E5%AD%90%E5%BA%A7A%E6%A0%8B8%E6%A5%BC8-8");
//        map.put("address", "光电园双子座A栋8楼8-8");
//        map.put("district", "渝北区");
        map.put("name", "渝北区");

        String json = new Gson().toJson(map);
        Map<String, String> resultMap = MakeSign.makeSign(json);

        String urlParams = resultMap.get("urlParams");
        System.out.println("urlParams == " + urlParams);

        String upperCase = resultMap.get("upperCase");
        System.out.println("upperCase == " + upperCase);

        String md5 = resultMap.get("md5");
        System.out.println("md5 == " + md5);

        String sign = resultMap.get("sign");
        System.out.println("sign == " + sign);


    }
}
