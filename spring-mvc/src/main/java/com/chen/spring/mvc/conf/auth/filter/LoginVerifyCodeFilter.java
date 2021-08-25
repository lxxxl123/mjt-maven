package com.chen.spring.mvc.conf.auth.filter;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author chenwh
 * @date 2021/8/24
 * 校验验证码
 */
@Slf4j
public class LoginVerifyCodeFilter implements Filter {

    public AntPathRequestMatcher matcher = new AntPathRequestMatcher("/login","POST");

    public String codeParameter = "verify_code";

    private Cache<String, String> cache;

    public LoginVerifyCodeFilter(Cache<String,String> cache){
        this.cache = cache;
    }

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

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
        String sessionId = request.getRequestedSessionId();
        if (code.equals(cache.getIfPresent(sessionId))) {
            chain.doFilter(request, response);
        } else {
            String encode = URLEncoder.encode("验证码有误", "utf-8");
            redirectStrategy.sendRedirect(request, response, "/v1/login?error=" + encode);
        }
        cache.invalidate(code);

    }

}
