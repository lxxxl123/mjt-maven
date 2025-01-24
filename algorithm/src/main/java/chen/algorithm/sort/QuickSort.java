package chen.algorithm.sort;

import java.util.Arrays;

/**
 * 思路：基数选择+双指针
 * time complexity: O nlog(n)
 * space complexity: O(nlog(n)) , 递归栈空间
 * stability: no
 */
public class QuickSort {

    public void sort(int[] arr) {
        sort(arr, 0, arr.length - 1);

    }

    /**
     * 每次循环都把基数放在正确的位置 ， 也就是   [i1,i2,base,j1,j2] ; 其中  i < base < j
     * @param l arr[i]为基数 ， l为左指针
     * @param r r 为右指针
     */
    public void sort(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        // 选中最左位基数
        int p = arr[l];
        int left = l;
        int right = r;
        while (l < r) {
            while  (p <= arr[r] && l < r) {
                r--;
            }
            while (arr[l] <= p && l < r) {
                l++;
            }
            if (l < r) {
                swap(arr, l, r);
            }
        }
        int idx = l;
        swap(arr, left, idx);
        sort(arr, left, idx-1);
        sort(arr, idx + 1, right);
    }

    public void swap(int[] arr, int l, int r) {
        int temp = arr[l];
        arr[l] = arr[r];
        arr[r] = temp;
    }


    public static void main(String[] args) {
        int[] arr = new int[]{5, 1, 2, 3, 4, 6, 7};
        new QuickSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
