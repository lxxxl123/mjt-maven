package chen.util;

import java.util.*;
import java.util.function.Function;

public class BiSet {


    public BiSet() {

    }

    public <T> BiSet(Collection<T> list, Function<T, Object>... funcs) {
        addAll(list, funcs);
    }

    Set<List> set = new HashSet<>();

    public void add(Object... key) {
        set.add(Arrays.asList(key));
    }

    public boolean contain(Object... keys) {
        return set.contains(Arrays.asList(keys));
    }

    public <T> void addAll(Collection<T> list , Function<T, Object> ... func){
        for (T o : list) {
            List<Object> keys = new ArrayList<>();
            for (int i = 0; i < func.length; i++) {
                keys.add(func[i].apply(o));
            }
            set.add(keys);
        }
    }

}
