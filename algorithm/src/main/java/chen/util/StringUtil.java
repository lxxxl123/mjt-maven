package chen.util;

/**
 * @author chenwh3
 */
public class StringUtil {

    /**
     * 空字符转换
     */
    public static String toNotNullStr(Object object) {
        if (null != object) {
            return object.toString().trim();
        } else {
            return "";
        }
    }

}
