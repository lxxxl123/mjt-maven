package com.chen.spring.mvc.conf.auth.login;

import com.github.benmanes.caffeine.cache.Cache;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    private ObjectMapper objectMapper = new ObjectMapper();


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
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        String loginPageHtml = exception.getMessage();
//        response.setContentType("text/html;charset=UTF-8");
//        response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
//        response.getWriter().write(loginPageHtml);
//        request.getRequestDispatcher("/abcde")
//                .forward(request, response);
//        response.sendRedirect("error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
