package com.example.ucms.service;

import com.example.ucms.dto.AuthResponse;
import com.example.ucms.model.User;
import com.example.ucms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//handle services related to authentication
@Service
public class AuthService {

    @Autowired
    private UserRepo repo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //register new user
    public User register(User user) {
        System.out.println(user.getName());
        if (repo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exists with this email");
        }
        user.setRole_id(2L);
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    //verify user details
    public AuthResponse verify(User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            AuthResponse authResponse = new AuthResponse();
            Optional<User> userinfo = repo.findByEmail(user.getEmail());
            if (userinfo.isPresent()) {
                System.out.println("blpm "+userinfo.get().getUserId());
            };
            System.out.println("hiii"+(userinfo.isPresent()?userinfo.get().getEmail():"null"));
            authResponse.setRole(userinfo.isPresent() ? userinfo.get().getRole_id() : null);
            authResponse.setToken(jwtService.generateToken(userinfo.get().getUserId()));
            System.out.println("nama ethanama "+jwtService.extractUserName(authResponse.getToken()));
            return authResponse;
        }
        throw new RuntimeException("Bad credentials");
    }

    public String validateToken(String token) {
        //todo
        return "hi";
    }
}

