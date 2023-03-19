package com.chen.extensions.java.lang.String;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenwh3
 */
@Extension
public final class StringExt {

    public static String[] split(@This String str, char separator) {
        return StringUtils.split(str, separator);
    }




    public static boolean eq(@This String str, String s) {
        return StringUtils.equals(str, s);
    }

    public static boolean eqAny(@This String str, String... s) {
        return StringUtils.equalsAny(str, s);
    }
}