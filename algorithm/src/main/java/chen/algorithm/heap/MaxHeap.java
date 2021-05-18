package chen.algorithm.heap;

import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * @author chenwh
 * @date 2021/5/12
 */

public class MaxHeap {

    /**
     * 堆排序
     * eg::
     * l = 5
     * tree:
     * ----------------------
     * 0
     * 1    2
     * 3    4
     * ----------------------
     */
    public static void sort(int[] arr) {
        int l = arr.length;
        //遍历所有非叶子节点
        for (int i = l / 2 - 1; i >= 0; i--) {
            adjustHead(i, arr, l);
        }

        for (int i = 0; i < l-1; i++) {
            swag(arr, 0, l - i - 1);
            adjustHead(0, arr, l - i - 1);
        }
    }

    public static void swag(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 若i节点不合理 , 并且左右儿子都是大顶堆 , 则可以使用该函数调整
     */
    public static void adjustHead(int fa, int[] arr, int len) {
        for (int son = 2 * fa + 1; son < len; ) {
            if (son + 1 < len && arr[son + 1] > arr[son]) {
                son++;
            }
            if (arr[son] > arr[fa]) {
                //交换父子值
                swag(arr, son, fa);
                fa = son;
                son = 2 * fa + 1;
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {3, 4, 6, 2, 3, 4, 1, 4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }


}
