package com.example.ucms.dto;

import lombok.Data;

@Data
public class ResultsRequest {
    private String userId;
    private String courseCode;
    private String grade;
}
