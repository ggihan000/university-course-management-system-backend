package com.example.ucms.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String cpassword;
    private String npassword;
    private String email;
}
