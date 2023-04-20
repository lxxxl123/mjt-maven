package chen.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static String[] parseCommaSeparatedString(String str) {
        if (str == null || str.isEmpty()) {
            return new String[0];
        }
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        int parenCount = 0; // 记录左括号数量
        int braceCount = 0; // 记录左大括号数量
        int bracketCount = 0; // 记录左中括号数量
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c == '\'' || c == '\"') && !inQuotes) { // 开始引号
                inQuotes = true;
                sb.append(c);
            } else if ((c == '\'' || c == '\"') && inQuotes && (i == str.length() - 1 || str.charAt(i + 1) != c)) { // 结束引号
                inQuotes = false;
                sb.append(c);
            } else if (c == '(' && !inQuotes && braceCount == 0 && bracketCount == 0) { // 左圆括号
                parenCount++;
                sb.append(c);
            } else if (c == ')' && !inQuotes && braceCount == 0 && bracketCount == 0) { // 右圆括号
                parenCount--;
                sb.append(c);
            } else if (c == '{' && !inQuotes && parenCount == 0 && bracketCount == 0) { // 左大括号
                braceCount++;
                sb.append(c);
            } else if (c == '}' && !inQuotes && parenCount == 0 && bracketCount == 0) { // 右大括号
                braceCount--;
                sb.append(c);
            } else if (c == '[' && !inQuotes && parenCount == 0 && braceCount == 0) { // 左中括号
                bracketCount++;
                sb.append(c);
            } else if (c == ']' && !inQuotes && parenCount == 0 && braceCount == 0) { // 右中括号
                bracketCount--;
                sb.append(c);
            } else if (c == ',' && !inQuotes && parenCount == 0 && braceCount == 0 && bracketCount == 0) { // 逗号
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            result.add(sb.toString().trim());
        }
        return result.toArray(new String[0]);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(parseCommaSeparatedString("( 123,, ) , 1234'123,'")));
    }

}
