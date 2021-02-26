package chen;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 全排列 , 全组合 , 部分排列 , 部分组合
 * 全排列
 * 经典排列组合算法 , 有序 , 不可重复使用
 * @author chenwh
 * @date 2021/2/25
 */

public class OrderedRank {

    public List<List<String>> rank(List<String> list) {
        list.sort(Comparator.naturalOrder());
        ArrayList<List<String>> rest = new ArrayList<>();
        rank(rest, new LinkedList<>(), list);
        return rest;
    }

    private void rank(List<List<String>> res, LinkedList<String> selected, List<String> selectAble) {
        if (selected.size() == selectAble.size()) {
            res.add(new ArrayList<>(selected));
            return ;
        }

        String last = null;
        for (int i = 0; i < selectAble.size(); i++) {
            String cur = selectAble.get(i);
            if (cur != null && !cur.equals(last)) {
                selectAble.set(i, null);
                selected.add(cur);
                rank(res, selected, selectAble);
                selected.pollLast();
                selectAble.set(i, cur);
            }
            last = cur;
        }
    }

    public static void main(String[] args) {
        System.out.println(new OrderedRank().rank(Stream.of("a", "b","b","c").collect(Collectors.toList())));

    }
}
