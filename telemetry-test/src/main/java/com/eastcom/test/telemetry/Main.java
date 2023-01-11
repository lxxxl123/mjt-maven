package com.eastcom.test.telemetry;


import com.eastcom.test.telemetry.session.Session;
import lombok.extern.slf4j.Slf4j;


/**
 * @author chenwh
 * @date 2021/12/1
 */
@Slf4j
public class Main {

    /**
     * 以下代码仅用于支持telemetry的h3c设备
     * @link https://www.h3c.com/cn/Service/Document_Software/Document_Center/Home/Switches/00-Public/Trending/White_Paper/Telemetry_White_Paper-6W100/
     */
    public static void main(String[] args) {
        Session session = Session.builder()
                .ip("192.168.99.254")
                .port(50051)
                .username("ipnet")
                .passowrd("admin")
                .build();

        session.connect();
        session.display("tcp");
        //其他接口没有详细的说明文档 , 未验证
//        session.getClient().subscribeByStreamName()
//        session.getClient().getEventReport()
        session.close();
    }}
