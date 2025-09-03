package com.example.ucms.dto;

import lombok.Data;

@Data
public class CourseRequest {
    private String course_title;
    private String course_description;
    private String year;
    private String semester;
    private String subject;
}
