package com.chen;

import java.util.List;

/**
 * @author chenwh3
 */
public class Main {
    public static void main(String[] args) {
        char[] a = new char[]{'a', 'b', 'c'};
        int[] b = new int[]{1, 2, 3, 4};
        List ints = b.toList();
        System.out.println(ints);

    }
}