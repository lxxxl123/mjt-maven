package com.chen.spring.mvc.conf.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/8/24
 */
@Configuration
public class CacheConfig {


    @Bean("lockedUser")
    public Cache<String,Byte> lockedUser(){
        return (Cache<String, Byte>) (Object)
                (Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(Duration.ofMinutes(5))
                        .build());
    }


    @Bean("verifyCodeCache")
    public Cache<String, String> verifyCodeCache () {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }
}
