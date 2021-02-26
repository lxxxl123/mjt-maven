package chen;

/**
 * KMP算法  , 用于匹配子串 , 核心思路:有限状态机
 *
 * @author chenwh
 * @date 2021/2/26
 */
public class KMP {

    /**
     * 低级kmp , 效率一般
     */
    private int search(String haystack , String needle){
        if (needle.length() == 0) {
            return 0;
        }
        int[][] dp = new int[needle.length()][27];
        int X = 0;
        dp[0][needle.charAt(0)-97] = 1;
        for (int i = 1; i < dp.length; i++) {
            //当前状态需要的字符
            int need = needle.charAt(i) - 97;
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j] = dp[X][j];
            }
            dp[i][need] = i + 1;
            X = dp[X][need];
        }

        int cur = 0;
        int end = needle.length();
        for (int i = 0; i < haystack.toCharArray().length; i++) {
            cur = dp[cur][haystack.charAt(i) - 97];
            if (cur == end) {
                return i - needle.length() + 1;
            }
        }
        return -1;
    }


    public int strStr1(String ss, String pp) {
        if (pp.isEmpty()) return 0;

        // 分别读取原串和匹配串的长度
        int n = ss.length(), m = pp.length();
        // 原串和匹配串前面都加空格，使其下标从 1 开始
        ss = " " + ss;
        pp = " " + pp;

        char[] s = ss.toCharArray();
        char[] p = pp.toCharArray();

        // 构建 next 数组，数组长度为匹配串的长度（next 数组是和匹配串相关的）
        int[] next = new int[m + 1];
        // 构造过程 i = 2，j = 0 开始，i 小于等于匹配串长度 【构造 i 从 2 开始】
        for (int i = 2, j = 0; i <= m; i++) {
            // 匹配不成功的话，j = next(j)
            while (j > 0 && p[i] != p[j + 1])
                j = next[j];
            // 匹配成功的话，j++
            if (p[i] == p[j + 1]) j++;
            next[i] = j;
        }

        // 匹配过程，i = 1，j = 0 开始，i 小于等于原串长度 【匹配 i 从 1 开始】
        for (int i = 1, j = 0; i <= n; i++) {
            // 匹配不成功 j = next(j)
            while (j > 0 && s[i] != p[j + 1]) j = next[j];
            // 匹配成功 j++
            if (s[i] == p[j + 1]) j++;
            // 如果匹配成功了，直接返回
            if (j == m) return i - m;
        }

        return -1;
    }

    public int strStr2(String haystack, String needle) {
        //两种特殊情况
        if (needle.length() == 0) {
            return 0;
        }
        if (haystack.length() == 0) {
            return -1;
        }
        // char 数组
        char[] hasyarr = haystack.toCharArray();
        char[] nearr = needle.toCharArray();
        //长度
        int halen = hasyarr.length;
        int nelen = nearr.length;
        //返回下标
        return kmp(hasyarr,halen,nearr,nelen);

    }


    public int kmp (char[] hasyarr, int halen, char[] nearr, int nelen) {
        //获取next 数组
        int[] next = next(nearr,nelen);
        int j = 0;
        for (int i = 0; i < halen; ++i) {
            //发现不匹配的字符，然后根据 next 数组移动指针，移动到最大公共前后缀的，
            //前缀的后一位,和咱们移动模式串的含义相同
            while (j > 0 && hasyarr[i] != nearr[j]) {
                j = next[j - 1] + 1;
                //超出长度时，可以直接返回不存在
                if (nelen - j + i > halen) {
                    return -1;
                }
            }
            //如果相同就将指针同时后移一下，比较下个字符
            if (hasyarr[i] == nearr[j]) {
                ++j;
            }
            //遍历完整个模式串，返回模式串的起点下标
            if (j == nelen) {
                return i - nelen + 1;
            }
        }
        return -1;
    }
    //这一块比较难懂，不想看的同学可以忽略，了解大致含义即可，或者自己调试一下，看看运行情况
    //我会每一步都写上注释
    public  int[] next (char[] needle,int len) {
        //定义 next 数组
        int[] next = new int[len];
        // 初始化
        next[0] = -1;
        int k = -1;
        for (int i = 1; i < len; ++i) {
            //我们此时知道了 [0,i-1]的最长前后缀，但是k+1的指向的值和i不相同时，我们则需要回溯
            //因为 next[k]就时用来记录子串的最长公共前后缀的尾坐标（即长度）
            //就要找 k+1前一个元素在next数组里的值,即next[k+1]
            while (k != -1 && needle[k + 1] != needle[i]) {
                k = next[k];
            }
            // 相同情况，就是 k的下一位，和 i 相同时，此时我们已经知道 [0,i-1]的最长前后缀
            //然后 k - 1 又和 i 相同，最长前后缀加1，即可
            if (needle[k+1] == needle[i]) {
                ++k;
            }
            next[i] = k;

        }

        return next;
    }

    public static void main(String[] args) {
        System.out.println(new KMP().strStr1("ababc","abc"));
    }
}
