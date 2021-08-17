package com.chen.spring.mvc.conf.webConfig;

import com.chen.spring.mvc.model.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenwh
 * @date 2021/8/16
 */

@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception e){
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result<?> handleException2(Exception e){
        return Result.error(e.getMessage());
    }
}
