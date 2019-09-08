package com.qjkji.orderproject.core.sms;

import com.qjkji.orderproject.core.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SmsAuthentication implements Authentication {
    private User user;

    public SmsAuthentication(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
