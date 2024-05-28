package chen.algorithm.sort;

/**
 * 希尔排序 (缩小增量排序) ， 插入排序的一种
 * time complexity O(n^1.3) 慢于 O(nlog2n)
 */
public class ShellSort {
    public static void sort(int[] arr) {
        int temp, j;
        for (int path = arr.length / 2; path > 0; path /= 2) {
            for (int i = path; i < arr.length; i++) {
                j = i;
                temp = arr[j];
                while (j - path >= 0 && temp < arr[j - path]) {
                    arr[j] = arr[j - path];
                    j -= path;
                }
                arr[j] = temp;
            }
        }
    }

    public static void main(String[] args) {

    }
}
