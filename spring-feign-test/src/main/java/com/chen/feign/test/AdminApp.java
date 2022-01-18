package com.chen.feign.test;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenwh
 * @date 2022/1/27
 */
@SpringBootApplication
@EnableAdminServer
//@EnableDiscoveryClient
public class AdminApp {
    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class,args);
    }
}
