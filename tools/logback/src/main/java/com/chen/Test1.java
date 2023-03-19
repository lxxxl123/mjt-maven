package com.chen;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.impl.StaticLoggerBinder;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.stream.Stream;

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
        LoggerContext loggerFactory =(LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
        Iterator<Appender<ILoggingEvent>> appenderIterator = loggerFactory.getLoggerList().get(0).iteratorForAppenders();
        while (appenderIterator.hasNext()) {
            Appender<ILoggingEvent> next = appenderIterator.next();
            if (next instanceof ConsoleAppender) {
                Encoder<ILoggingEvent> encoder = ((ConsoleAppender<ILoggingEvent>) next).getEncoder();
                encoder.stop();
                ((PatternLayoutEncoder) encoder).setPattern("[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%le] [T:%t] [C:%c{1} %L] %X{prefix}%X{sessionId}%X{traceId}%X{user}%X{ne}%X{test} %m %n");
                encoder.start();
                System.out.println(encoder);
            }
        }


//        MDC.put("sessionId", " [sessionId:53a1898b-247f-4fd4-bca7-cca8e216b147]");
//        MDC.put("ne"," [ne:device-25]");
//        MDC.put("user"," [user:dxcj]");
//        MDC.put("test","test str ");
        MDC.put("prefix","kkkkksdfadsf");
//        CatServiceLogUtils.initTraceId();
        Test2.log("123%X{sessionId}");
//        System.out.println(new Test1().getA());
    }

}
