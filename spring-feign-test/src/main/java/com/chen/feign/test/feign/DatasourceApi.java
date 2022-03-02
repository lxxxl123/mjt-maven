package com.chen.feign.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "test1", url = "http://172.16.100.73:8109", path = "")
public interface DatasourceApi {

    @GetMapping("/v1/datasources/name/")
    public String getNe(@RequestParam("name") String name);
}
