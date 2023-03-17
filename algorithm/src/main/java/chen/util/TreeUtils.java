package chen.util;

import chen.util.support.tree.TreeBuilder;
import chen.util.support.tree.TreeConfig;
import chen.util.support.tree.TreeNode;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author chenwh3
 */
public class TreeUtils {

    public static <T> List<TreeNode<T>> buildTree(List<T> list, List<Function<T, String>> getKeys) {
        return new TreeBuilder().buildTree(list, getKeys);
    }

    public static <T> List<TreeNode<T>> buildTree(List<T> list, List<Function<T, String>> getKeys, TreeConfig treeConfig) {
        return new TreeBuilder().config(treeConfig).buildTree(list, getKeys);
    }

    public static <T> List<TreeNode<T>> addRoot(List<TreeNode<T>> list, String rootName )  {
        int pid = -10;
        int id = -1;
        int depth = 0;
        TreeConfig config = TreeConfig.DEFAULT_CONFIG;
        if (CollUtil.isNotEmpty(list)) {
            config = list.get(0).getConfig();
            id = Convert.toInt(list.get(0).getPid());
            depth = list.get(0).getDepth() - 1;
        }
        return ListUtil.of(new TreeNode<>(id, pid, (T) null, rootName, depth, list, false, config));
    }

    /**
     * 会重新生成id , 合并多棵树
     */
    public static <T> List<TreeNode<T>> merge(List<TreeNode<T>>... trees) {
        return mergeDiff((List[]) trees);

    }

    /**
     * 合并多颗树
     */
    public static List<TreeNode<?>> mergeDiff(List<? extends TreeNode>... trees) {
        List<TreeNode<?>> list = new ArrayList<>();
        for (List<? extends TreeNode> tree : trees) {
            for (TreeNode<?> treeNode : tree) {
                list.add(treeNode);
            }
        }
        AtomicInteger i = new AtomicInteger(0);

        forEach((List) list, (parent, node) -> {
            int pid = -10;
            if (parent != null) {
                pid = Convert.toInt(parent.getId());
            }
            node.setId(i.getAndIncrement());
            node.setPid(pid);
        });
        return list;
    }

    /**
     * 广度优先遍历
     * @param consumer t1 父节点 , t2 当前遍历节点
     */
    private static <T> void forEach(List<TreeNode<T>> list, BiConsumer<TreeNode<T>, TreeNode<T>> consumer) {
        LinkedList<List<TreeNode<T>>> iter = new LinkedList<>();
        LinkedList<TreeNode<T>> parent = new LinkedList<>();
        iter.add(list);
        List<TreeNode<T>> cur;
        List<TreeNode<T>> res = new ArrayList<>(list.size());
        while ((cur = iter.poll()) != null) {
            TreeNode<T> p = parent.poll();
            for (TreeNode<T> node : cur) {
                consumer.accept(p, node);
                if (CollUtil.isNotEmpty(node.getChildren())) {
                    iter.add(node.getChildren());
                    parent.add(node);
                }
                res.add(node);
            }
        }
    }

    public static <T> TreeNode<T> getFirstChild(TreeNode<T> t){
        if (t == null || t.isLeaf() || CollUtil.isEmpty(t.getChildren())) {
            return t;
        }
        return getFirstChild(t.getChildren().get(0));
    }


    /**
     * 广度优先遍历
     * @param consumer t1 第一个叶子节点 , t2 当前遍历节点
     */
    public static <T> void forEachForFirstChild(List<TreeNode<T>> list, BiConsumer<TreeNode<T>, TreeNode<T>> consumer) {
        LinkedList<List<TreeNode<T>>> iter = new LinkedList<>();
        iter.add(list);
        List<TreeNode<T>> cur;
        List<TreeNode<T>> res = new ArrayList<>(list.size());
        while ((cur = iter.poll()) != null) {
            for (TreeNode<T> node : cur) {
                consumer.accept(getFirstChild(node), node);
                if (CollUtil.isNotEmpty(node.getChildren())) {
                    iter.add(node.getChildren());
                }
                res.add(node);
            }
        }
    }

}
