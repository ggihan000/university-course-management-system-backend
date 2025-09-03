package com.example.ucms.dto;

import com.example.ucms.model.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long role;
}

