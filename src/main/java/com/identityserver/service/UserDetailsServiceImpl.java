package com.identityserver.service;

import com.identityserver.mapper.ManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by machenggong on 2019/8/5.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ManageMapper manageMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String password = manageMapper.getPasswordByUserName(userName);
        List<String> roles = manageMapper.listUserRolesByUserName(userName);
        if (password == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", userName));
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        UserDetails userDetails = new User(userName, password, authorities);
        System.out.println(userDetails.getAuthorities());
        return userDetails;
    }
}
