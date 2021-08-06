package com.chen.spring.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author chenwh
 * @date 2021/8/5
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/hello")
    public String hello(@RequestHeader(value = "test",required = false) String test){
        return test;
    }
}
