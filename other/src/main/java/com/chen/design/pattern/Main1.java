package com.chen.design.pattern;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chenwh
 * @date 2021/7/29
 */

public class Main1 {

    public static Map<Integer, List<byte[]>> rem = new HashMap<>();
    private static List<String> repeats = new ArrayList<>(11);
    public static void main(String[] args) {
        String s = "";
        for (int i = 0; i < 11; i++) {
            repeats.add(s);
            s += "*";
        }
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            long time = System.currentTimeMillis();
            String[] line = in.nextLine().split(" ");
            int present = Integer.parseInt(line[0]);
            int child = Integer.parseInt(line[1]);
            List<byte[]> res = dfs(present, child);
            System.out.println(res.size());
            for (byte[] re : res) {
                StringBuilder sb = new StringBuilder();
                System.out.println(re.length);
                
                for (int i = 0; i < re.length; i++) {
                    sb.append(repeats.get(re[i]));
                    if (i != re.length - 1) {
                        sb.append("|");
                    }
                }
//                System.out.println(sb.toString());
            }
            System.out.println("耗时 = " + (System.currentTimeMillis() - time) / 1);
        }
    }

    public static Integer getKey(int k, int n) {
        return 100 * k + n;
    }


    public static String repeat(String s, int times) {
        return repeats.get(times);
    }


    public static List<byte[]> dfs(int present, int child){
        List<byte[]> res;
        if ((res = rem.get(getKey(present, child))) != null) {
            return res;
        }
        if (child == 0) {
            return null;
        }
        if (child == 1) {
            return Stream.of(new byte[]{(byte) present}).collect(Collectors.toList());
        }
        if (present == 0) {
            byte[] temp = new byte[child];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = 0;
            }
            return Stream.of(temp).collect(Collectors.toList());
        }
        List<byte[]> list = new ArrayList<>();
        //决定分给当前小朋友多少个礼物

        for (int i = 0; i <= present; i++) {
            byte cur = (byte) i;
            List<byte[]> next = dfs(present - i, child-1);
            if (next == null) {
                list.add(new byte[]{(byte) i});
            }else{
                for (byte[] s : next) {
                    byte[] temp = new byte[s.length + 1];
                    temp[0] = cur;
                    for (int j = 0; j < s.length; j++) {
                        temp[j + 1] = s[j];
                    }
                    list.add(temp);
                }
            }
        }
        res = list;
        rem.put(getKey(present, child), res);
        return res;
    }
}
