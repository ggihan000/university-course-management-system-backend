package com.example.ucms.service;

import com.example.ucms.dto.AuthResponse;
import com.example.ucms.dto.ChangePasswordRequest;
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

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        System.out.println(changePasswordRequest.getNpassword()+changePasswordRequest.getCpassword());
        Optional<User> userinfo = repo.findByEmail(changePasswordRequest.getEmail());
        if (userinfo.isEmpty()) {
            throw new RuntimeException("Bad Credentials");
        }
        User user = userinfo.get();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), changePasswordRequest.getCpassword()));

        System.out.println("jhdjhgfd");
        if(authentication.isAuthenticated()){
            user.setPassword(encoder.encode(changePasswordRequest.getNpassword()));
            repo.save(user);
        }else{
            throw new RuntimeException("Bad Credentials");
        }
    }

    //verify user details
    public AuthResponse verify(User user) {
        Optional<User> userinfo = repo.findByEmail(user.getEmail());
        if (userinfo.isEmpty()) {
            throw new RuntimeException("Bad Credentials");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userinfo.get().getUserId(), user.getPassword()));

        if (authentication.isAuthenticated()) {

            AuthResponse authResponse = new AuthResponse();

            authResponse.setRole(userinfo.get().getRole_id());
            authResponse.setToken(jwtService.generateToken(userinfo.get().getUserId()));
            return authResponse;
        }
        throw new RuntimeException("Bad credentials");
    }

    public String validateToken(String token) {
        //todo
        return "hi";
    }
}

