package chen.util;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BracketFind {

    char left = '(';
    char right = ')';

    Set<Character> charSet = new HashSet<>();

    private BracketFind(){
        /**
         * 单双引号视为转义
         */
        charSet.add('"');
        charSet.add('\'');
    }


    public int findBracket(String str , int start) {

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
                    return i;
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
    int strLeft = 0;

    int strRight = 0;

    public int left(){
        return strLeft;
    }
    public int right(){
        return right;
    }
    public boolean find(String s, String regex){
        Pattern ptn = Pattern.compile(regex);
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
