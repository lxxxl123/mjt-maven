package chen;


import java.util.*;


/**
 * @author chenwh
 * @date 2021/4/2
 */

class Solution {
    public int findLengthOfLCIS(int[] nums) {
        int len = nums.length;
        int[] dp = new int[len];
        Arrays.fill(dp,1);
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
                max = Math.max(dp[i], max);
            }
        }

        return max;
    }

    public static void main(String[] args) {
        int arr[] = {1,3,6,7,9,4,10,5,6};
        int res = new Solution().findLengthOfLCIS(arr);
        System.out.println(res);
    }
}