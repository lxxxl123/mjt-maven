package com.chen.spring.mvc.conf.auth.event;

/**
 * @author chenwh
 * @date 2021/8/25
 */

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthSuccessEvent implements ApplicationListener<AuthenticationSuccessEvent> {
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        System.out.println("reset user : " + username);
    }
}
