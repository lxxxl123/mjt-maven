package com.chen;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HtmlUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Test {


    public static String formatHtml(String s) {
        return HtmlUtil.cleanHtmlTag(s.replaceAll("\n]", "]"));
    }
    private static String cutString(String value,int num) {
        StringBuffer sb = new StringBuffer();
        int curLen = 0;
        int totalLen = value.length();
        try {
            for (int i = 0; i < totalLen && curLen <= num; i++) {
                String temp = value.substring(i, i + 1);
                curLen += temp.getBytes("GBK").length;
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }


    public static Map<String,String> test(){

        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("1", "1");
            return map;
        } finally {
            map.put("2", "2");
        }
    }

    public static void main(String[] args) throws Exception{
        Snowflake snow = new Snowflake();
        for (int i = 0; i < 100; i++) {
            System.out.println(snow.nextId());
        }

    }


}
