package com.chen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;

/**
 * @author chenwh
 * @date 2021/3/2
 */
@SpringBootApplication
@RestController
public class MainStart {
    public static void main(String[] args) {
        SpringApplication.run(MainStart.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        System.out.println(1 / 0);
        return "12345";
    }
}
