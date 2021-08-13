package com.chen.spring.mvc.controller;

import com.chen.spring.mvc.model.User;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * @author chenwh
 * @date 2021/8/5
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/hello")
    public String hello(@RequestHeader(value = "test",required = false) String test, @RequestParam(value = "name",required = false) String name){
        System.out.println(name);
        return test;
    }


    @PostMapping("/hello")
    public String hello(@RequestBody User user) {
        System.out.println(user);
        return user.getToken();
    }
}
