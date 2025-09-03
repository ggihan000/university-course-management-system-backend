package com.example.ucms.controller;

import com.example.ucms.dto.AddStudentResponse;
import com.example.ucms.dto.CourseRequest;
import com.example.ucms.dto.ResultsRequest;
import com.example.ucms.model.User;
import com.example.ucms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/add_students")
    public ResponseEntity<?> add_students(@RequestBody User user) {
        System.out.println("add_students");
        System.out.println("kkkkk");
        try {
            AddStudentResponse asr = adminService.addStudent(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "student", asr));
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/add_courses")
    public ResponseEntity<?> add_courses(@RequestBody CourseRequest course) {
        try {
            String c_code = adminService.addCourse(course);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", "Course Code is "+c_code));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/add_results")
    public ResponseEntity<?> add_results(@RequestBody ResultsRequest result) {
        try {
            adminService.addResults(result);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", "Results Added Successfully"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
