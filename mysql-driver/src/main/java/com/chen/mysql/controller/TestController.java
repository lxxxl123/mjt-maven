package com.chen.mysql.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author chenwh
 * @date 2022/1/6
 */
@Slf4j
@RestController("/")
public class TestController {

    @Resource
    private JdbcTemplate jdbcTemplate;


    @GetMapping("test")
    public void test() throws Exception {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from test1");
        log.info("{}", maps);
    }
}
