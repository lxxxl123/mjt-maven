package chen;

/**
 * 经典算法:二分法
 * @author chenwh
 * @date 2021/1/19
 */

public class BiSection {

    /**
     * 用二分法找第最后一个小于target的数
     */
    public static int lBisect(int[] arr, int target) {
        int left = 0;
        int right = arr.length;
        //1.代表找不到目标值得情况
        int tar = -1;
        //2. 必须小于等于 , 代表即使范围缩小到1个数依然进行检查
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (arr[mid] < target) {
                left = mid + 1;
                tar = mid;
            } else {
                right = mid - 1;
            }
        }
        return tar;
    }

    /**
     * 找出第一个大于target的数
     */
    public static int rBisect(int []arr , int target){
        int left = 0;
        int right = arr.length;
        int tar = -1;
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (target < arr[mid]) {
                right = mid - 1;
                tar = mid;
            }else{
                left = mid + 1;
            }
        }
        return tar;

    }

    public static void main(String[] args) {
        System.out.println(rBisect(new int[]{1, 2, 3, 3, 3, 3, 4}, 2));

    }


}
