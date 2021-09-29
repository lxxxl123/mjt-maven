package com.chen.design.model.action.command;

/**
 * @author chenwh
 * @date 2021/9/29
 */

public class Client {

    /**
     * mode: Command
     */
    public static void main(String[] args) {
        Runnable cmd = () -> System.out.println("run");
        cmd.run();
    }
}
