package com.qjkji.orderproject.core.service;

import com.qjkji.orderproject.core.entity.User;
import com.qjkji.orderproject.core.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.selectUserByName(s);
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        user.setAuthorities(new ArrayList<>());
        return user;
    }

}
