package chen.algorithm.sort;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 桶排序
 * 应用场景: 需要获取计算某个区间值的关系
 * 计数排序,基数排序,升级版
 * 时间复杂读接近O(n)
 */
public class BuckSort {

    public static void sort(List<Integer> arr) {
        Integer max = Collections.max(arr);
        Integer min = Collections.min(arr);
        //桶的取值范围 min-min+size ..... max-size - max
        int size = 5;
        //桶的数量
        int count = (max - min) / size + 1;
        List<List<Integer>> bucks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            bucks.add(new ArrayList<>());
        }

        for (Integer num : arr) {
            int idx = (num - min) / size;
            bucks.get(idx).add(num);
        }

        int i = 0;
        for (List<Integer> buck : bucks) {
            buck.sort(Comparator.naturalOrder());
            for (Integer num : buck) {
                arr.set(i, num);
                i++;
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> arr = Stream.of(4, 27, 9, 4, 3, 5, 1, 1, 1, 2, 4).collect(Collectors.toList());
        sort(arr);
        System.out.println(arr);
    }

}
