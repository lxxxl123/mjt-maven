package com.chen.spring.mvc.conf.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenwh
 * @date 2021/8/24
 * 校验验证码
 */
@Slf4j
public class LoginVerifyCodeFilter implements Filter {

    public AntPathRequestMatcher matcher = new AntPathRequestMatcher("/login","POST");

    public String codeParameter = "verify_code";


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (!matcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }
        String code = request.getParameter(codeParameter);
        code = code == null ? "" : code;
        log.info("receive sessionId = " + request.getRequestedSessionId());

        chain.doFilter(request, response);
    }

}
