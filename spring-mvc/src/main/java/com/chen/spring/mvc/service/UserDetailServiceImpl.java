package com.chen.spring.mvc.service;

import com.chen.spring.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenwh
 * @date 2021/8/19
 */

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService  {

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode("1234"));
        user.setAuthorities(AuthorityUtils.createAuthorityList("admin"));
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        return user;
    }
}
