package chen.algorithm.sort;

import java.util.Arrays;

/**
 * 堆排序
 * time complexity: O(nlogn)
 * space complexity: O(1)
 * stability: no
 *
 * 思路：基数选择+双指针
 * 把数组看成堆
 * 假设父节点index = fa
 * - son1 = fa/2
 * - son2 = fa/2 + 1
 *
 *  最小的非叶子节点索引 = arr.len / 2 . 因为假设最终叶子节点索引为 n , 其父节点必然是 (n-1)/2
 */
public class HeapSort {

    public void sort(int[] arr) {
        int l = arr.length;
        for (int i = l / 2; i >= 0; i--) {
            adjust(arr, i, arr.length);
        }

        for (int i = 0; i < l ; i++) {
            swap(arr, 0, arr.length - i - 1);
            adjust(arr, 0, arr.length - i - 1);
        }

    }

    /**
     * 调整有问题的父节点
     * @param arr
     * @param fa
     */
    public void adjust(int[] arr, int fa , int l) {
        while (fa < l) {
            int son = fa * 2 + 1;
            if (son >= l) {
                break;
            }
            int bro = son + 1;
            if (bro < l && arr[bro] > arr[son]) {
                son = bro;
            }
            if (arr[son] > arr[fa]) {
                swap(arr, son, fa);
                fa = son;
            } else {
                break;
            }
        }
    }

    public void swap(int[] arr, int l, int r) {
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
    }


    public static void main(String[] args) {
        int[] arr = new int[]{2, 4, 1, 3, 7, 4, 8, 0};
        new HeapSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
