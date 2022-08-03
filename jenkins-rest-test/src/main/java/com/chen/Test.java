package com.chen;

import cn.hutool.http.HtmlUtil;

import java.util.Arrays;

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


}
