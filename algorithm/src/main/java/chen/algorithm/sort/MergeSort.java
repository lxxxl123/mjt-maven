package chen.algorithm.sort;

import java.util.Arrays;

/**
 * 归并排序 O(logN) 递归实现
 * space complexity O(N) 栈空间
 * 应用场景 : 内存外排序
 * 其他功能 : 可以找出每个元素 左侧或右侧大于或小于改元素的所有值或数量 -- 用于寻找逆序对
 * @see "https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof"
 * @author chenwh
 */

public class MergeSort {

    public static void sort(int[] arr) {
        int[] temp = new int[arr.length];
        sort(arr, 0, arr.length, temp);
    }

    //自顶向下
    public static void sort(int[] arr, int left, int right, int[] temp) {
        if (right - left > 1) {
            int mid = (right + left) >> 1;
            sort(arr, left, mid, temp);
            sort(arr, mid, right, temp);
            merge(arr, left, mid, right, temp);
        }
    }

    //升序
    public static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int l = left;
        int r = mid;
        for (int i = left; i < right; i++) {
            //双指针找较少值并复制到temp[]中
            if (r == right || (l != mid && arr[l] < arr[r])) {
                temp[i] = arr[l];
                l++;
            } else {
                temp[i] = arr[r];
                r++;
            }
        }

        if (right - left >= 0) System.arraycopy(temp, left, arr, left, right - left);
    }

    public static void main(String[] args) {
        int[] arr = new int[]{4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 3, 3, 3, 9};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}

