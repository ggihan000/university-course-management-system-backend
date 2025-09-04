package com.example.ucms.controller;


import com.example.ucms.dto.EnrollmentRequest;
import com.example.ucms.dto.GetCourseDetailResponse;
import com.example.ucms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/get_course_details")
    public ResponseEntity<?> getCourseDetails(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            System.out.println("dfjdkf");
            GetCourseDetailResponse getCourseDetailResponse = studentService.getCourseDetails(userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", getCourseDetailResponse));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@AuthenticationPrincipal UserDetails userDetails,@RequestBody EnrollmentRequest enrollmentRequest) {
        try {
            studentService.enrollCourse(enrollmentRequest.getCourse_id(),userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", "Enrolled Successfully"));
        }catch (Exception e) {
            System.out.println("error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/unenroll")
    public ResponseEntity<?> unenroll(@AuthenticationPrincipal UserDetails userDetails,@RequestBody EnrollmentRequest enrollmentRequest) {
        try {
            studentService.unEnrollCourse(enrollmentRequest.getCourse_id(), userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("success", true,
                            "message", "unEnrolled Successfully"));
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

}
