package com.chen.spring.mvc.conf.webConfig;

import com.chen.spring.mvc.model.User;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chenwh
 * @date 2021/8/12
 */

public class MyMessageConverter extends MappingJackson2HttpMessageConverter {


    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }



    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        User u = ((User) super.read(type, contextClass, inputMessage));
        List<String> tokens = inputMessage.getHeaders().get("token");
        if (tokens != null && tokens.size() > 0) {
            u.setToken(tokens.get(0));
        }
        return u;
    }
}
