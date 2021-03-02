package com.chen.advice.abc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenwh
 * @date 2021/3/2
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler
    String handleControllerException(HttpServletRequest request, Throwable e) {
        log.error("", e);
        return "error";
    }
}
