package com.example.ucms.controller;

import com.example.ucms.dto.AuthResponse;
import com.example.ucms.dto.ChangePasswordRequest;
import com.example.ucms.model.User;
import com.example.ucms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            authService.changePassword(changePasswordRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", "Password Changed Successfully"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false,
                            "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try{
            AuthResponse authResponse = authService.verify(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", "Login successful",
                            "token", authResponse.getToken(),
                            "role", (authResponse.getRole()==1L?"ADMIN":"STUDENT")));
        } catch (RuntimeException e) {
            // Handle invalid email password
            System.out.println("error: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false,
                            "message", e.getMessage()));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Internal server error"
                    ));
        }
    }
}
