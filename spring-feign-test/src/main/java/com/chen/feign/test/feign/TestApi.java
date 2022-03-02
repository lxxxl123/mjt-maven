package com.chen.feign.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "test", url = "http://localhost:8081", path = "")
public interface TestApi {

    @GetMapping("/getName")
    public String getName(@RequestParam("name") String name);
}
