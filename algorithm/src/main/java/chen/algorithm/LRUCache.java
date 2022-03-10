package chen.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 固定capacity , 逐出最久未使用的元素
 *  LinkedHashMap有天然的实现 , 继承并重写removeEldestEntry则可
 * 本质 : 双向链表+hashMap
 */

public class LRUCache {

    public LRUCache(int capacity) {

    }

    public int get(int key) {
        return 0;
    }

    public void put(int key, int value) {

    }
    public static void main(String[] args) {
        Map<Integer,Integer> map = new LinkedHashMap(){
            @Override
            protected boolean removeEldestEntry(Map.Entry entry) {
                return size() > 3;
            }
        };
        map.put(1, 1);
        map.put(2, 1);
        map.put(3, 1);
        map.get(3);
        map.get(2);
        map.put(4, 1);
        System.out.println(map);


    }
}
