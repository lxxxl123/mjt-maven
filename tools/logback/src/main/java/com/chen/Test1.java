package com.chen;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.nio.charset.Charset;

/**
 * @author chenwh
 * @date 2021/5/19
 */

public class Test1 {

    @Getter
    private int a = 1;

    public static int test(int a) {
        return ~a;
    }

    public static void main(String[] args) {
        MDC.put("sessionId", " [sessionId:53a1898b-247f-4fd4-bca7-cca8e216b147]");
        MDC.put("ne"," [ne:device-25]");
        MDC.put("user"," [user:dxcj]");
//        CatServiceLogUtils.initTraceId();
        Test2.log("123%X{sessionId}");
//        System.out.println(new Test1().getA());
    }

}
