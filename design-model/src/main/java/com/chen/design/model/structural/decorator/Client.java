package com.chen.design.model.structural.decorator;

import java.io.*;

/**
 * @author chenwh
 * @date 2021/9/26
 */

public class Client {

    /**
     * Name: 装饰器模式
     * Example: OutputStream , InputStream
     * Effect: 对原有对象新增功能
     * @param args
     */
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(".")));
        while (true) {
            String line = reader.readLine();
            System.out.println(line);
        }

    }
}
