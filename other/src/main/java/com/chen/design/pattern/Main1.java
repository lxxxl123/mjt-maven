package com.chen.design.pattern;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author chenwh
 * @date 2021/7/29
 */

public class Main1 {

    public static Map<Integer, String[]> rem = new HashMap<>();
    private static List<String> repeats = new ArrayList<>(11);
    public static void main(String[] args) {
        String s = "";
        for (int i = 0; i < 11; i++) {
            repeats.add(s);
            s += "*";
        }
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String[] line = in.nextLine().split(" ");
            int present = Integer.parseInt(line[0]);
            int child = Integer.parseInt(line[1]);
            String[] res = dfs(present, child);
            System.out.println(res.length);
            for (String re : res) {
                System.out.println(re);
            }
        }
    }

    public static Integer getKey(int k, int n) {
        return 100 * k + n;
    }


    public static String repeat(String s, int times) {
        return repeats.get(times);
    }


    public static String[] dfs(int present, int child){
        if (child == 0) {
            return null;
        }
        if (child == 1) {
            return new String[]{repeat("*", present)};
        }
        if (present == 0) {
            return new String[]{""};
        }
        String[] res;
        if ((res = rem.get(getKey(present, child))) != null) {
            return res;
        }
        List<String> list = new ArrayList<>();
        //决定分给当前小朋友多少个礼物

        for (int i = 0; i <= present; i++) {
            String cur = repeat("*", i);
            String []next = dfs(present - i, child-1);
            if (next == null) {
                list.add(cur);
            }else{
                for (String s : next) {
                    list.add(String.format("%s|%s", cur, s));
                }
            }
        }
        res = list.toArray(new String[]{});
        rem.put(getKey(present, child), res);
        return res;
    }
}
