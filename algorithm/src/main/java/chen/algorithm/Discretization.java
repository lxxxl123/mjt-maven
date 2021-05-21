package chen.algorithm;


import java.util.Arrays;


/**
 * 离散化
 * 常规实现方式 , 排序+二分法
 */
public class Discretization {
    public int[] discretize(int[] arr){
        int[] tmp = Arrays.copyOf(arr,arr.length);
        Arrays.sort(tmp);
        for (int i = 0; i < arr.length; i++) {
            int idx = Arrays.binarySearch(tmp, arr[i]);
            arr[i] = idx;
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.binarySearch(new int[]{1, 2, 3, 3, 3}, 3));
    }
}
