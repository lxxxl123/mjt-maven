package com.chen.spring.gateway.filer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author chenwh
 * @date 2021/8/3
 */

@Component
@Slf4j
public class MyFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst("TOKEN");
        if (cookie != null) {
            // NOTE: 服务间调用使用服务名，restTemplate必须加@LoadBalanced注解
            return chain.filter(exchange);
//            Boolean ok = template.postForObject(authUrl, cookie.getValue(), Boolean.class);
//            if (ok != null && ok) {
//                return chain.filter(exchange);
//            }
        }
        String redirectUrl = "http://172.16.100.73:9999/dispatcher/#/dispatcher/systemAccountManagement";
        log.info("bmg 重定向到URL: {}", redirectUrl);
        response.getHeaders().set(HttpHeaders.LOCATION, redirectUrl);
        response.getHeaders().set("Authorization", "Bearer 123456");
        //303状态码表示由于请求对应的资源存在着另一个URI，应使用GET方法定向获取请求的资源
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.setComplete();
    }
    @Override
    public int getOrder() {
        return 1;
    }
}