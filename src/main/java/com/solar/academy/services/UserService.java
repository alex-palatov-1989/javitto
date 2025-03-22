package com.solar.academy.services;


import com.solar.academy.dao.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        var user = repository.getById(id);
        if(Objects.isNull(user))
            throw new UsernameNotFoundException(" can find "+ id);

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
