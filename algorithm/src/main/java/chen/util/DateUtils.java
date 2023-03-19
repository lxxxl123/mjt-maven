package chen.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class DateUtils extends cn.hutool.core.date.DateUtil {

    public static final String PATTERN_DATE_MONTH = "yyyy-MM";


    /**
     * 自动判断日期格式
     */
    public static Date parse(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof String) {
            String dateStr = (String) obj;
            if (dateStr.length() == 7 ) {
                DateUtil.parse(dateStr, PATTERN_DATE_MONTH);
            }
            return DateUtil.parse(dateStr);
        }
        return null;
    }
}
