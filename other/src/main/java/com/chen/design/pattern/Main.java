package com.chen.design.pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * @author chenwh
 * @date 2021/7/29
 */

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String[] line = in.nextLine().split(" ");

            int year = Integer.parseInt(line[0]);
            int month = Integer.parseInt(line[1]) - 1;
            int day = Integer.parseInt(line[2]);

            Calendar begin = Calendar.getInstance();
            begin.set(year, 0, 0,0,0,0);
            Calendar now = Calendar.getInstance();
            now.set(year, month, day,0,0,0);
            System.out.println((now.getTime().getTime() - begin.getTime().getTime()) / (24 * 60 * 60 * 1000));

        }




    }
}
