package com.chen.spring.mvc.conf.auth.login;

import com.github.benmanes.caffeine.cache.Cache;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
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
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    private RequestRateLimiter requestRateLimiter;

    @Resource(name = "lockedUser")
    private Cache<String, Byte> lockedUser;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        try {
            String username;
            if (requestRateLimiter.overLimitWhenIncremented((username = request.getParameter("username")))) {
                lockedUser.put(username, null);
            }
        } catch (Exception e) { }
        super.onAuthenticationFailure(request, response, exception);
    }
}
