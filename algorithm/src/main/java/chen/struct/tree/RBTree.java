package chen.struct.tree;

import lombok.Data;

/**
 * @红黑树
 *
 * 原理:
 * 不可连续红 , 每条路径和黑数量必须相同
 * 相关概念
 * 左旋:以某个节点左旋 , 自己到左下 , 提拔儿子 , 孙子变儿子
 * 右旋:同理
 *
 * 增加的5种情况
 * 1.父节点为黑 , 不用动 ->END
 * 2.父节点为红 , 叔叔为红 -> 叔、父 变黑;祖父变红 -> END
 * 3.父红,叔黑,自己在右 -> 以父左旋 , 当前节点改成父
 * 4.父红,叔黑,自己在左 -> 父黑 , 祖父红 , 祖父右旋 ->END
 * 5.父红,是根 -> 父黑 ->END
 *
 * 删除
 *
 */
public class RBTree {

    private RBNode root;

    @Data
    public static class RBNode {
        private int val;
        private RBTree left;
        private RBTree right;
    }


    public void add(int val) {
        RBNode cur = root;


    }

    public boolean find(int val) {
        return true;
    }

    public void delete(int val) {

    }


    public static void main(String[] args) {

    }
}
