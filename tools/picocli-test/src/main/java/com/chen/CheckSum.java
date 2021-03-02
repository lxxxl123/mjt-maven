package com.chen;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

/**
 * @author chenwh
 * @date 2021/3/1
 */
@Command(name = "sftp", mixinStandardHelpOptions = true, version = "dispatcher 4.0",
        description = "指令平台sftp功能 , 用于 源机器(当前登录网元) 与 目标机器 之间文件的上传和下载")
public class CheckSum implements Callable<Integer> {

    @Parameters(index = "0", description = "需下载文件的路径")
    private String oriPath;

    @Parameters(index = "1", description = "保存位置")
    private String aimPath;

    @Option(names = {"-i", "--host"}, description = "目标机器ip地址", required = true)
    private String host;

    @Option(names = {"-p", "--port"}, description = "目标机器sftp端口号,默认22" )
    private int port = 22;

    @Option(names = {"-u", "--username"}, description = "目标机器用户名", required = true)
    private String username = "";

    @Option(names = {"-pw", "--password"}, description = "目标机器密码", required = true)
    private String password = "";


    @Override
    public Integer call() throws Exception { // your business logic goes here...
        System.out.println(1 / 0);
        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        String s = "sftp /home/ipnet/test/test1.txt /home/ipnet/test/test2.txt -i 111111 -u 33333 -pw 22222";
        String[] arg = s
                .substring("sftp".length(), s.length())
                .trim()
                .split("\"?\\s+\"?");
        CommandLine commandLine = new CommandLine(new CheckSum());

        int execute = commandLine.setParameterExceptionHandler((a, b) -> {
            System.out.println(a.getMessage());
            System.out.println(commandLine.getUsageMessage());
            return -1;
        }).setExecutionExceptionHandler((ex, cmd, var) -> {
            System.out.println(ex.getMessage());
            return -2;
        })
        .execute(arg);
        System.out.println(execute);

    }
}
