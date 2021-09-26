package com.chen.design.model.structural.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/9/26
 */

public class Client {

    private static Map<String, String> map = new HashMap<>();

    public static String getInstance(String type) {
        String s = map.get(type);
        if (s == null) {
            s = new String("instance" + type);
            map.put(type, s);
        }
        return s;

    }

    /**
     * Name: 享元模式
     * 目的: 减少内存的使用
     */
    public static void main(String[] args) throws Exception {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            s.add(getInstance("123"));
        }
        TimeUnit.SECONDS.sleep(10000);
    }
}
