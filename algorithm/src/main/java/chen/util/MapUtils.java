package chen.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapUtils {

    /**
     * 对一个list多次分组 , 效果同Collectors.groupBy
     * @param datas
     * @param getMethods
     * @param <T>
     * @return
     */
    public  static <T,E> Map getMapTree(List<T> datas, E nullKey , Function<T,E>... getMethods){
        HashMap<E, Object> head = new HashMap<>();
        for (T data : datas) {
            HashMap<E, Object> map = head;
            for (int i = 0; i < getMethods.length; i++) {
                E key = Optional.ofNullable(getMethods[i].apply(data)).orElse(nullKey);
                if (i == getMethods.length - 1) {
                    map.putIfAbsent(key, new ArrayList<>());
                    ((List) map.get(key)).add(data);
                } else {
                    map.putIfAbsent(key, new HashMap<>());
                    map = (HashMap<E, Object>) map.get(key);
                }
            }
        }
        return head;

    }


    public  static <T,E> Map getMapTree(List<T> datas, E nullKey , List<Function<T,E>> getMethods
            ,Comparator<?> ...comparators){

        TreeMap<E, Object> head = new TreeMap(comparators[0]);
        for (T data : datas) {
            TreeMap<E, Object> map = head;
            for (int i = 0; i < getMethods.size(); i++) {
                E key = Optional.ofNullable(getMethods.get(i).apply(data)).orElse(nullKey);
                if (i == getMethods.size() - 1) {
                    map.putIfAbsent(key, new ArrayList<>());
                    ((List) map.get(key)).add(data);
                } else {
                    map.putIfAbsent(key, new TreeMap(comparators[i+1]==null?Comparator.naturalOrder():comparators[i+1]));
                    map = (TreeMap<E, Object>) map.get(key);
                }
            }
        }
        return head;

    }

    public  static <T,E> Map getMapTree(List<T> datas, E nullKey , List<Function<T,E>> getMethods
            , Supplier<? extends Map<E,Object>> newMap){

        Map<E, Object> head = newMap.get();
        for (T data : datas) {
            Map<E, Object> map = head;
            for (int i = 0; i < getMethods.size(); i++) {
                E key = Optional.ofNullable(getMethods.get(i).apply(data)).orElse(nullKey);
                if (i == getMethods.size() - 1) {
                    map.putIfAbsent(key, new ArrayList<>());
                    ((List) map.get(key)).add(data);
                } else {
                    map.putIfAbsent(key, newMap.get());
                    map = (TreeMap<E, Object>) map.get(key);
                }
            }
        }
        return head;

    }

}
