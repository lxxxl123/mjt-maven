package com.chen.spring.mvc.service;

import com.chen.spring.mvc.model.User;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private UserService userService;

    @Resource(name = "lockedUser")
    private Cache<String, Byte> lockedUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在");
        }

        if (lockedUser.getIfPresent(username) != null) {
            throw new LockedException("失败次数过多,账号被锁");
        }

        return user;
    }
}
