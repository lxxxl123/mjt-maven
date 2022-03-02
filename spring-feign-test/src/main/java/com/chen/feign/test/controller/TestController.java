package com.chen.feign.test.controller;

import com.chen.feign.test.feign.DatasourceApi;
import com.chen.feign.test.feign.TestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chenwh
 * @date 2022/2/15
 */
@RestController
public class TestController {

    @Resource
    private TestApi testApi;

    @Resource
    private DatasourceApi datasourceApi;

    @GetMapping("/getName")
    public String getName(@RequestParam("name") String name){
        return name;
    }

    @GetMapping("/test")
    public String test(@RequestParam("name") String name){
        return testApi.getName(name);
    }

    @GetMapping("/getNe")
    public String getNe(@RequestParam("name") String name){
        return datasourceApi.getNe(name);
    }
}
