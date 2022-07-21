package chen;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author chenwh
 * @date 2021/4/2
 */

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> map =  new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            }
            map.put(target - nums[i], i);
        }
        return null;
    }

    public static void main(String[] args) {
        int arr[] = {2,7,4,3} ;
        int[] ints = new Solution().twoSum(arr, 9);
        System.out.println(Arrays.toString(ints));
    }
}