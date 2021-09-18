package com.chen.rsocket.test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chenwh
 * @date 2021/9/16
 */

public class Main {

    public static void main(String[] args) {
        List<Integer> collect = Stream.of(1, 2, 3).collect(Collectors.toList());
        System.out.println(collect);
    }
}
