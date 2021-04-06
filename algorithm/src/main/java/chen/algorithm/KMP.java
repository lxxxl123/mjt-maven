package chen.algorithm;

/**
 * KMP算法  , 用于匹配子串 , 核心思路:有限状态机
 *
 * @author chenwh
 * @date 2021/2/26
 */
public class KMP {

    /**
     * 思路:有限状态机 , 在pattern字符串数量有限的时候效率较高
     */
    public int strStr(String haystack , String needle){
        if (needle.length() == 0) {
            return 0;
        }
        int l1 = haystack.length();
        int l2 = needle.length();

        int[][] dp = new int[l2][27];
        int X = 0;
        dp[0][needle.charAt(0)-97] = 1;
        for (int i = 1; i < l2; i++) {
            //当前状态需要的字符
            int need = needle.charAt(i) - 97;
            for (int j = 0; j < 27; j++) {
                if (need == j) {
                    dp[i][need] = i + 1;
                } else {
                    dp[i][j] = dp[X][j];
                }
            }
            X = dp[X][need];
        }

        int cur = 0;
        int end = needle.length();
        for (int i = 0; i < l1; i++) {
            cur = dp[cur][haystack.charAt(i) - 97];
            if (cur == end) {
                return i - needle.length() + 1;
            }
        }
        return -1;
    }


    /**
     * 思路:半-有限状态机 , 效率相对高一点 , 在字haystack符长度不多的情况下
     */
    public int strStr2(String haystack, String needle) {
        if (needle.equals("")) {
            return 0;
        }

        int l2 = needle.length();
        int l1 = haystack.length();
        //失败跳转值
        int[] next = new int[l2];
        char[] chars = needle.toCharArray();
        for (int i = 0; i < next.length; i++) {
            next[i] = -1;
        }
        for (int i = 1, x = 0; i < l2; i++) {
            //找上一个可以匹配的值
            while (x > 0 && chars[x] != chars[i]) {
                x = next[x - 1] + 1;
            }

            //x位置匹配成功 , 下一个
            if (chars[x] == chars[i]) {
                next[i] = x;
                x++;
            }
        }

        char[] ctx = haystack.toCharArray();
        for (int i = 0, x = 0; i < l1; i++) {
            while (x > 0 && chars[x] != ctx[i]) {
                x = next[x-1]+1;
            }
            if (chars[x] == ctx[i]) {
                x++;
            }
            if (x == chars.length) {
                return i - x + 1;
            }
        }
        return -1;
    }




    public static void main(String[] args) {
        System.out.println(new KMP().strStr2("bbbbbbbbbbbbbbbbbbaaaaaaaaaaaa",
                "aaaaaaaaaaa"));
    }
}
