package chen.struct;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 单调栈
 * 作用:
 * 1.在数组中批量列出元素的在其相对位置分别在左右两侧找出第一个比它大/小的元素 , 只需要遍历一次!
 * @see <a href="https://leetcode-cn.com/problems/largest-rectangle-in-histogram/</a>
 */
public class MonoStack {

    public static void main(String[] args) {
        List<Integer> l = Stream.of(1, 2, 3, null).collect(Collectors.toList());
        for (Integer integer : l) {
            System.out.println(integer);
        }
    }
}
