package chen;


import java.util.PriorityQueue;


/**
 * @author chenwh
 * @date 2021/4/2
 */

class Solution {
    public int magicTower(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum < 0) {
            return -1;
        }
        int res = 0;
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        long hp = 0;
        for (int num : nums) {
            hp += num;
            if (num < 0) {
                minHeap.add(num);
            }
            if (hp < 0) {
                res++;
                hp -= minHeap.poll();
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().magicTower(new int[]{1, 2, 3, 4, 5}));
        PriorityQueue<Integer> list = new PriorityQueue<>();
        list.add(2);
        list.add(1);
        System.out.println(list.poll());

    }
}