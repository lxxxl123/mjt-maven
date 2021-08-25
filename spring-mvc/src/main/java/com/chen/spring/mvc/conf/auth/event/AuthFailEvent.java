package com.chen.spring.mvc.conf.auth.event;

/**
 * @author chenwh
 * @date 2021/8/25
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthFailEvent implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        log.info("userName = {},  event = {}", event.getAuthentication().getName(), event);
    }
}
