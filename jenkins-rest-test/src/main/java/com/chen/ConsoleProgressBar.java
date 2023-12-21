package com.chen;

import java.util.Arrays;

/**
 * @author chenwh3
 */
public class ConsoleProgressBar {

    public static void printBar(int total, int now) {
        // 参数校验
        check(total, now);
        // 计算百分比
        double percent = (double) now / total * 100;
        // 计算格数，默认100格
        int fillNum = (int) percent;
        // 生成进度条
        String bar = generateBar(fillNum);
        // 输出
        System.out.printf("\rProgress [%s] %.2f%%", bar, percent);
        if (fillNum == 100) System.out.print('\n');
    }

    public static void printBar(int total, int now, String elapse) {
        // 参数校验
        check(total, now);
        // 计算
        double percent = (double) now / total * 100;
        // 计算格数，默认100格
        int fillNum = (int) percent;
        // 生成进度条
        String bar = generateBar(fillNum);
        // 输出
        System.out.printf("\rProgress [%s] %.2f%% | elapse: %s", bar, percent, elapse);
        if (fillNum == 100) System.out.print('\n');
    }

    public static String generateBar(int total, int fillNum, char c) {
        char[] chars = new char[total];
        Arrays.fill(chars, 0, fillNum, c);
        Arrays.fill(chars, fillNum, total, ' ');
        return String.valueOf(chars);
    }

    private static String generateBar(int fillNum) {
        return generateBar(100, fillNum, '#');
    }

    private static void check(int total, int now) {
        if (total < now) throw new IllegalArgumentException("total can't smaller than now");
        if (total < 1) throw new IllegalArgumentException("total can't smaller than 1");
        if (now < 0) throw new IllegalArgumentException("now can't smaller than 0");
    }

    public static void main(String[] args) {
        printBar(100,40);
        printBar(100,60);
        System.out.println("123");
        printBar(100,90);
        System.out.println("123");
    }

}