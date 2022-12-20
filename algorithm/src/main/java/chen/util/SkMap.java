package chen.util;

import java.util.Collections;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author chenwh3
 */
public class SkMap<K, V> extends ConcurrentSkipListMap<K,V> {

    private Class<?> keyClass;

    public ConcurrentNavigableMap<K, V> like(String from) {
        if (this.isEmpty()) {
            return this;
        }
        if (keyClass == null) {
            keyClass = this.firstEntry().getKey().getClass();
        }

        if (String.class.isAssignableFrom(keyClass)) {
            return this.subMap((K) from, (K) (from + Character.MAX_VALUE));
        } else {
            throw new RuntimeException("参数有误");
        }
    }



    public static void main(String[] args) {
        SkMap<String, String> map = new SkMap<>();
        map.put("123", "23123");
        map.put("234", "23123");
        System.out.println(map.like("1"));

    }
}
