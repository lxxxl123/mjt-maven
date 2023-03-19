package com.chen.sap.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * schedulejob
 *
 * @author yangzj2
 * @date 2019/9/6
 */
public class CommonManage {
    public static final long uploadSize = 550000;
    public static final String uploadSizeStr = "500KB";

    /**
     * 字符判断是否为空,true:空 false:非空
     */
    public static boolean isNull(String str) {
        if (null != str && !"".equals(str.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 字符判断是否非空, true:非空 false:空
     */
    public static boolean notNull(String str) {
        if (null != str && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 空字符转换
     */
    public static String toNotNullString(Object object) {
        String rn = "";
        if (object != null) {
            rn = object.toString();
        }
        return rn;
    }

    /**
     * 获取系统时间
     */
    public static String getSysTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取系统时间
     */
    public static String getSysDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取系统日期"yyyy-MM-dd"（cyz于20100319增加）
     */
    public static String getSysDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }
    /**
     * 获取系统年份
     */
    public static String getSysYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取系统 年月 yyMM
     */
    public static String getSysYYMM() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取当天之前days天的日期 （零时零分零秒）
     * 如果当前时间为   2011-09-25 10:10:10
     * 则 dayBeforeToday(5) 结果为2011-09-20 00:00:00
     */
    public static String dayBeforeToday(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);//24小时制，而calendar.set(Calendar.HOUR,0);则是12小时制
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.HOUR,-24*days);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取当天之后days天的日期 （零时零分零秒）
     * 如果当前时间为   2011-09-25 10:10:10
     * 则 dayAfterToday(1) 结果为2011-09-26 00:00:00
     */
    public static String dayAfterToday(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.HOUR,24*days);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取一个uuid
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }
}