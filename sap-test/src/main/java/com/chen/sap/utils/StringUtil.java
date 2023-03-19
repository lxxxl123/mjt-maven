package com.chen.sap.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {


    /**
     * 空字符转换
     */
    public static String toNotNullStr(Object object) {
        String rn = "";
        try {
            if (null != object) {
                rn = object.toString().trim();
            }
        } catch (Exception ignored) {
        }
        return rn;
    }
}
