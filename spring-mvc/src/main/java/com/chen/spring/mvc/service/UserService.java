package com.chen.spring.mvc.service;

import com.chen.spring.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author chenwh
 * @date 2021/8/19
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private PasswordEncoder passwordEncoder;


    @Value("#{${userlist.users}}")
    private Map<String, String> users;


    /**
     * 测试环境变量
     */
    @Autowired
    public void test(){
        users.forEach((k,v)->{
            log.info("key = {},v = {}", k, v);
        });
    }

    public User getUserByName(String userName){
        if (!users.containsKey(userName)) {
            return null;
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(users.get(userName)));
        user.setUsername(userName);
        return user;
    }



}
