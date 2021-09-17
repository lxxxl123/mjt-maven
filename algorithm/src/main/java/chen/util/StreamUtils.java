package chen.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chenwh
 * @date 2021/9/17
 */

public class StreamUtils {

    public static <T> void split(List<T> list , int splitSize , Consumer<List<T>> consumer) {
        if (list == null && list.size() == 0) {
            return;
        }
        Stream.iterate(0, n -> n + 1).limit((int)Math.ceil((double) list.size()/splitSize)).forEach(e->{
            consumer.accept(list.stream().skip(e * splitSize).limit(splitSize).collect(Collectors.toList()));
        });
    }

    public static <T> List<List<T>> split(List<T> list , int splitSize) {
        if (list == null || list.size() == 0) {
            return null;
        }
        return Stream.iterate(0, n -> n + 1)
                .limit((int) Math.ceil((double) list.size() / splitSize))
                .map(e -> list.stream().skip(e * splitSize).limit(splitSize).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            list.add(i + "");
        }
        List<List<String>> split = split(list, 3);
        for (int i = 0; i < split.size(); i++) {
            System.out.println(split.get(i));
        }

    }
}
