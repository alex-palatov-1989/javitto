package com.solar.academy.services;


import com.solar.academy.dao.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        var user = repository.getById(id);

        GrantedAuthority tmp;
        var creads = new User(
                user.getName(),
                user.getPassword(),
                List.of( user.getRole() )
        );
        return creads;
    }
    @Autowired
    UserRepository repository;
}
