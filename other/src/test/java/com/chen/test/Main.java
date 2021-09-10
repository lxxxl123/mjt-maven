package com.chen.test;

import java.util.Calendar;
import java.util.Scanner;

/**
 * @author chenwh
 * @date 2021/8/28
 */

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String[] line = in.nextLine().split(" ");
            int year = Integer.parseInt(line[0]);
            int month = Integer.parseInt(line[1]);
            int day = Integer.parseInt(line[2]);

            Calendar begin = Calendar.getInstance();
            begin.set(year, 0, 0);
            Calendar now = Calendar.getInstance();
            now.set(year, month, day);
            System.out.println(begin.getTime().getDay());
            System.out.println(now.getTime().getDay());
        }




    }
}
