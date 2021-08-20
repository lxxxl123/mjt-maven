package com.chen.spring.mvc.conf.auth;

/**
 * @author chenwh
 * @date 2021/8/17
 */

import com.chen.spring.mvc.conf.auth.login.LoginFailHandler;
import com.chen.spring.mvc.conf.auth.login.LoginSuccessHandler;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;

/**
 * <pre>
 *      Spring Security配置类
 * </pre>
 *
 * <pre>
 * @author mazq
 * 修改记录
 *    修改后版本:     修改人：  修改日期: 2020/06/15 10:39  修改内容:
 * </pre>
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private LoginFailHandler loginFailHandler;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    public RequestRateLimiter requestLimitRule() {
        return new InMemorySlidingWindowRequestRateLimiter(
                Collections.singleton(RequestLimitRule.of(Duration.ofMinutes(5), 5)));
    }

    @Bean("lockedUser")
    public Cache<String,Byte> lockedUser(){
        return (Cache<String, Byte>) (Object)
                (Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build());
    }

    @Bean("authenticationManagerBean")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {    //auth.inMemoryAuthentication()
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//        auth.inMemoryAuthentication()
//                .withUser("nicky")
//                .password("{noop}123")
//                .roles("admin");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/asserts/**");
        web.ignoring().antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http   // 配置登录页并允许访问
                .formLogin()
//                    .failureHandler(loginFailHandler)
//                    .successHandler(loginSuccessHandler)
                // 配置Basic登录
                //.and().httpBasic()
                // 配置登出页面
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .authorizeRequests()
                    .antMatchers("v1/**","/oauth/**", "/login**", "/logout/**")
                    .permitAll()
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                // 关闭跨域保护;
                .and().csrf().disable();
    }


}
