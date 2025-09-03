package com.example.ucms.service;

import com.example.ucms.model.User;
import com.example.ucms.model.UserPrincipal;
import com.example.ucms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repo.findByUserId(email);
        user.get();
        if (user == null) {
            System.out.println("no user found");
            throw new UsernameNotFoundException(email+" not found");
        }
        return new UserPrincipal(user.get());
    }



}

