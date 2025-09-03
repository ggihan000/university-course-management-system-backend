package com.example.ucms.dto;

import lombok.Data;

@Data
public class ResponseCourse {
    private String courseName;
    private String courseCode;
    private String courseDescription;
    private String grade = null;
}
