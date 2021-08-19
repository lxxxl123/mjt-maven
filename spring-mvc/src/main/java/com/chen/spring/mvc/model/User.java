package com.chen.spring.mvc.model;

import ch.qos.logback.core.subst.Token;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author chenwh
 * @date 2021/8/12
 */

@Data
public class User implements UserDetails {

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true ;

    private boolean isCredentialsNonExpired = true;

    private boolean isEnabled = true;
}
