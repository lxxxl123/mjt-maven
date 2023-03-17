package chen.util.support.tree;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * @author chenwh3
 */
public class TreeBuilder {

    private TreeConfig config ;


    public TreeBuilder(){
        this.config = TreeConfig.DEFAULT_CONFIG;
    }

    public TreeBuilder config(TreeConfig treeConfig){
        this.config = treeConfig;
        return this;
    }


    /**
     *
     */
    private  <T> List<TreeNode<T>> buildTree(Map<String, Object> map) {
        return buildTree(map, 0, new AtomicInteger(1), 1);
    }

    private <T> List<TreeNode<T>> buildTree(Map<String, Object> map, int pid, AtomicInteger atom, int depth) {
        List<TreeNode<T>> res = new ArrayList<>();
        for (String s : map.keySet()) {
            Object o = map.get(s);
            int id = atom.getAndIncrement();
            TreeNode<T> dto = new TreeNode<>(id, pid, (T) null, s, depth, Collections.EMPTY_LIST, false, config);
            res.add(dto);
            if (o instanceof List) {
                dto.setData(((List<T>) o).get(0));
                dto.setLeaf(true);
            } else if (o instanceof Map) {
                dto.setChildren(buildTree((Map) map.get(s), id, atom, depth + 1));
            }
        }
        return res;
    }


    private static <T, E> Map getMapTree(List<T> datas, List<Function<T, E>> getMethods
            , Supplier<? extends Map<E, Object>> newMap) {

        Map<E, Object> head = newMap.get();
        for (T data : datas) {
            Map<E, Object> map = head;
            for (int i = 0; i < getMethods.size(); i++) {
                E key = getMethods.get(i).apply(data);
                if (key == null) {
                    break;
                }
                if (i == getMethods.size() - 1) {
                    map.putIfAbsent(key, new ArrayList<>());
                    ((List) map.get(key)).add(data);
                } else {
                    map.putIfAbsent(key, newMap.get());
                    map = (Map<E, Object>) map.get(key);
                }
            }
        }
        return head;
    }

    public <T> List<TreeNode<T>> buildTree(List<T> list, List<Function<T, String>> getKeys) {
        Map mapTree = getMapTree(list, getKeys, () -> new LinkedHashMap<>());
        List<TreeNode<T>> res = buildTree(mapTree);
        return res;
    }
}
