package com.chen.spring.mvc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author chenwh
 * @date 2021/8/19
 */

public class TestBcryencode {



    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("{noop}secret"));
    }
}
