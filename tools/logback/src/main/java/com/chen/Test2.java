package com.chen;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author chenwh
 * @date 2021/5/19
 */

@Slf4j
public class Test2 {

   public static void log(String s){
      Logger logger = LoggerFactory.getLogger(Test1.class);
      logger.info(s);
   }
}


