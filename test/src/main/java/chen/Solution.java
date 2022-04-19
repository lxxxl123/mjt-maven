package chen;

import ch.qos.logback.core.pattern.parser.FormattingNode;

class Solution {
    public int minDistance(String word1, String word2) {
        int l1 = word1.length() + 1;
        int l2 = word2.length() + 1;
        int[][] dp = new int[l1][l2];
        for (int i = 0; i < l1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < l2; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < l1; i++) {
            for (int j = 1; j < l2; j++) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[i][j] = dp[i - 1][j - 2];
                }else{
                    dp[i][j] = Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[l1-1][l2 - 1];

    }
}