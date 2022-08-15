package com.chen;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;

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


    public static void main(String[] args) {

//        System.out.println(NumberUtil.div("268", "35.5"));
//        System.out.println(NumberUtil.div("179", "23.67"));
//
//        System.out.println(NumberUtil.div("281", "37.7"));
//        System.out.println(NumberUtil.div("184", "25.16"));
//        System.out.println(NumberUtil.div("202", "27.67")); //220
//        System.out.println(NumberUtil.div("231", "31.45"));
//        System.out.println(NumberUtil.div("219.56", "29.94"));
//
//        System.out.println(NumberUtil.div("170", "23.67"));
//        System.out.println(NumberUtil.div("262", "35.5"));

    }


}
