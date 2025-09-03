package com.example.ucms.dto;

import com.example.ucms.model.Course;
import lombok.Data;

import java.util.List;

@Data
public class GetCourseDetailResponse {
    List<ResponseCourse> EnrolledCourses;
    List<ResponseCourse> UnEnrolledCourses;
}
