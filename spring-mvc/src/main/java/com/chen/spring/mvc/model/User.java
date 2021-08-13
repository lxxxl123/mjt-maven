package com.chen.spring.mvc.model;

import ch.qos.logback.core.subst.Token;
import lombok.Data;

/**
 * @author chenwh
 * @date 2021/8/12
 */

@Data
public class User {
    String name;
    Integer age;
    String token;
}
