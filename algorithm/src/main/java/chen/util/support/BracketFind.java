package chen.util.support;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh3
 */
public class BracketFind {

    char left = '(';
    char right = ')';

    Set<Character> charSet = new HashSet<>();

    public BracketFind(){
        /**
         * 单双引号视为转义
         */
        charSet.add('"');
        charSet.add('\'');
    }


    private int findBracket(String str , int start) {

        /**
         * 0 - 这个状态下遇到()会把它当成字符串
         * 1 - 这个状态下遇到( 会尝试拆分
         */
        int num = -1;
        char[] chars = str.toCharArray();
        int state = 1;
        for (int i = start; i < chars.length; i++) {
            //找到与之匹配的右括号
            if (state == 1) {
                if (chars[i] == left) {
                    num = num == -1 ? 1 : num + 1;
                } else if (chars[i] == right) {
                    num--;
                }
                if (num == 0) {
                    return i+1;
                }
            }
            if (charSet.contains(chars[i])) {
                //取反
                state = ~state;
            }
        }
        return -1;
    }

    public String extract(String s, String regex) {
        Pattern ptn = Pattern.compile(regex);
        Matcher matcher = ptn.matcher(s);
        if (matcher.find()) {
            int left = matcher.start();
            int right = matcher.end();
            int end = findBracket(s, right);
            if (end <= 0) {
                return "";
            }
            return s.substring(left, end + 1);
        } else{
            return "";
        }
    }
    private int strLeft = 0;

    private int strRight = 0;

    private String s = "";

    public int left(){
        return strLeft;
    }
    public int right(){
        return strRight;
    }
    public String group(){
        return s.substring(strLeft, strRight);
    }
    public boolean find(String s, String regex){
        Pattern ptn = Pattern.compile(regex);
        return find(s, ptn);
    }

    /**
     * 从指定正则开始找配对的括号 , 并且跳过'' , "" 中的括号
     */
    public boolean find(String s, Pattern ptn){
        this.s = s;
        Matcher matcher = ptn.matcher(s);
        if (matcher.find()) {
            int left = matcher.start();
            int right = matcher.end();
            int end = findBracket(s, right);
            if (end <= 0) {
                return false;
            }
            strLeft = left;
            strRight = end;
            return true;
        } else{
            return false;
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println(new BracketFind().extract("(123) 1234 (ss '('s) ) ", "1234"));

    }
}
