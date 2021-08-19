package com.chen.spring.mvc.conf.auth.login;

import com.chen.spring.mvc.model.User;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenwh
 * @date 2021/8/19
 */
@Service
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Resource
    private RequestRateLimiter requestRateLimiter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            requestRateLimiter.resetLimit(((User) authentication.getPrincipal()).getUsername());
        } catch (Exception e) {
            log.error("",e);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
