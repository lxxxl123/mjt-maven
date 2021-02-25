package chen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 全排列 , 全组合 , 部分排列 , 部分组合
 * 部分组合 ,即 C(n,m)
 * @author chenwh
 * @date 2021/2/25
 */

public class UnOrderedRank {

    public List<List<String>> rank(List<String> list ,int len) {
        list.sort(Comparator.naturalOrder());
        ArrayList<List<String>> rest = new ArrayList<>();
        rank(rest, new LinkedList<>(), list, len, 0);
        return rest;
    }

    private void rank(List<List<String>> res, LinkedList<String> selected, List<String> selectAble, int len, int curIdx) {
        if (selected.size() == len) {
            res.add(new ArrayList<>(selected));
            return;
        }

        String last = null;
        for (int i = curIdx; i < selectAble.size(); i++) {
            String cur = selectAble.get(i);
            if (cur != null && !cur.equals(last)) {
                selected.add(cur);
                rank(res, selected, selectAble, len, i + 1);
                selected.pollLast();
            }
            last = cur;
        }
    }

    public static void main(String[] args) {
        System.out.println(new UnOrderedRank().rank(
                Stream.of("a", "b", "d", "c", "d").collect(Collectors.toList()), 2)
        );
    }
}
