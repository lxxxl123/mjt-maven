package com.chen.spring.mvc.conf.webConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenwh
 * @date 2021/8/2
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.chen"})
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {

        var resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {

        var freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates/");
        return freeMarkerConfigurer;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        ((ArrayList) converters).add(6, new MyMessageConverter());
        System.out.println(123);

    }
}