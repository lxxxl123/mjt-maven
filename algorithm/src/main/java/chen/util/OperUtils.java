package chen.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenwh
 * @date 2021/1/21
 */
@Slf4j
public class OperUtils {


    private static final String AND = "and";

    private static final String OR = "or";

    private static final String _AND_ = " and ";


    private static final String _OR_ = " or ";

    private static final String X_AND = " ^&^ ";

    private static final String X_OR = " ^|^ ";

    private static final String SPE = "^";

    private static class Tree {


        private final String[] splitOr;

        private final String stat;


        Tree(String stat){
            if (!AND.equals(stat)) {
                this.splitOr = stat.split("\\Q"+X_OR+"\\E");
            }else{
                this.splitOr = null;
            }
            this.stat = stat;

        }


        Tree(){
            this.stat = OR;
            this.splitOr = null;
        }

        private List<Tree> sons = new ArrayList<>();

        private void addSons(StringBuilder s) {
            if (SPE.equals(s.charAt(s.length() - 1) + "")) {
                s.deleteCharAt(s.length() - 1);
            }
            if (SPE.equals(s.charAt(0) + "")) {
                s.deleteCharAt(0);
            }

            Tree tree = new Tree(s.toString());
            s.delete(0, s.length());
            sons.add(tree);

        }

        private void addSons(Tree t) {
            sons.add(t);
        }

        private static String[] statAndStat(String[] split1,String[] split2){
            ArrayList<String> newlist = new ArrayList<>(split1.length*split2.length);
            for (String s1 : split1) {
                for (String s2 : split2) {
                    newlist.add(String.format("%s%s%s", s1, X_AND, s2));
                }
            }
            return newlist.toArray(new String[]{});
        }

        @Override
        public String toString() {
            try {
                if (AND.equals(stat)) {
                    return String.join(X_OR,sons.stream().map(e->e.splitOr).reduce(Tree::statAndStat).get());
                } else if (OR.equals(stat)) {
                    return sons.stream().map(Tree::toString).collect(Collectors.joining(X_OR));
                } else {
                    return stat;
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RuntimeException(e);
            }

        }

    }



    private static Tree createTree(String oper){
        char[] chars = oper.toCharArray();
        StringBuilder sb = new StringBuilder();
        Tree cur = new Tree();
        Tree root = cur;

        /**
         * 0 - 这个状态下遇到()会把它当成字符串
         * 1 - 这个状态下遇到( 会尝试拆分
         */
        int state = 1;
        for (int i = 0; i < chars.length; i++) {
            //找到与之匹配的右括号
            if (chars[i] == '(' && state == 1) {
                int num = 1;
                int right = -1;
                int left = i+1;
                for (int j = i+1; j < chars.length; j++) {
                    if (chars[j] == '(') {
                        num++;
                    } else if (chars[j] == ')') {
                        num--;
                    }
                    if (num == 0) {
                        right = j;
                        break;
                    }
                }
                sb.append(createTree(oper.substring(left, right)));
                i = right+1;
            }
            else if (chars[i] == '&' && i+1< chars.length && chars[i-1] == '^'  && chars[i + 1] == '^') {
                state = 1;
                if (AND.equals(cur.stat)) {
                    cur.addSons(sb);
                } else if (OR.equals(cur.stat) ) {
                    Tree son = new Tree(AND);
                    cur.addSons(son);
                    son.addSons(sb);
                    cur = son;
                }
                //跳过下一个^
                i += 1;
            } else if (chars[i] == '|' && i+1< chars.length && chars[i-1] == '^'  && chars[i + 1] == '^') {
                state = 1;
                if (AND.equals(cur.stat)) {
                    cur.addSons(sb);
                    cur = root;
                } else if (OR.equals(cur.stat)) {
                    cur.addSons(sb);
                }
                //跳过下一个^
                i += 1;
            } else {
                if (chars[i] != ' ') {
                    state = 0;
                }
                sb.append(chars[i]);
            }
            if (i >= chars.length-1) {
                cur.addSons(sb);
            }
        }

        return root;
    }


    /**
     * 去掉逻辑运算表达式的所有括号 , 但不进行运算
     * 只支持 and or
     */
    public static String[] splitBracket(String oper) {
        oper = tranAndOr(oper);
        return createTree(oper).toString().replace(X_AND, _AND_).split("\\Q"+X_OR+"\\E");
    }

    /**
     * 把有效的and 和 or 转化成 ^&^ 和 ^|^ , 规避"",''里的and 和 or
     */
    private static String tranAndOr(String oper) {
        oper = " " + oper + " ";
        int state = 0;
        int begin = 0;
        ArrayList<int[]> commonStr = new ArrayList<>();

        for (int i = 0; i < oper.toCharArray().length; i++) {
            char c = oper.charAt(i);
            if (c == '\'' && state == 0) {
                state = 1;
                commonStr.add(new int[]{begin, i});
            }else if (c == '\'' && state == 1){
                state = 0;
                begin = i;
            }
            else if (c == '"' && state == 0) {
                state = 2;
                commonStr.add(new int[]{begin, i});
            }else if (c == '"' && state == 2){
                state = 0;
                begin = i;
            } else if (i == oper.length() - 1) {
                commonStr.add(new int[]{begin, i});
            }
        }

        StringBuilder sb = new StringBuilder();
        int[] last = null;
        for (int i = 0; i < commonStr.size(); i++) {

            int[] ints = commonStr.get(i);
            if (last != null) {
                sb.append(oper.subSequence(last[1] + 1, ints[0]));
            }
            String s = oper.subSequence(ints[0], ints[1] + 1).toString();
            s = s.replace(_AND_, X_AND);
            s = s.replace(_OR_, X_OR);
            sb.append(s);
            last = ints;
        }

        return sb.toString();
    }

}
