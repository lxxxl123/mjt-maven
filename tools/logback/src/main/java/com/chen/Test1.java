package com.chen;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author chenwh
 * @date 2021/5/19
 */

@Slf4j
public class Test1 {

    public static void main(String[] args) {
//        MDC.put("sessionId", "53a1898b-247f-4fd4-bca7-cca8e216b147");
//        MDC.put("ne","device-25");
        MDC.put("user","dxcj");
        CatServiceLogUtils.initTraceId();
        log.info("message");

    }
}
