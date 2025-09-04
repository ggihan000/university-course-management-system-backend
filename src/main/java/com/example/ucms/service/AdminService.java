package com.example.ucms.service;


import com.example.ucms.dto.AddStudentResponse;
import com.example.ucms.dto.CourseRequest;
import com.example.ucms.dto.ResultsRequest;
import com.example.ucms.model.Course;
import com.example.ucms.model.Enrollment;
import com.example.ucms.model.User;
import com.example.ucms.repository.CoursesRepo;
import com.example.ucms.repository.EnrollmentRepo;
import com.example.ucms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private CoursesRepo coursesRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AddStudentResponse addStudent(User user) {

        if (userRepo.existsByNic(user.getNic())) {
            throw new RuntimeException("User already exists with this NIC");
        }

        user.setRole_id(2L);
        User saveduser = userRepo.save(user);

        String stu_num = user.getName().replace(" ","");

        if(saveduser.getId()>99) {
            stu_num = stu_num+saveduser.getId();
        }else if(saveduser.getId()>9) {
            stu_num = stu_num+"0"+saveduser.getId();
        }else {
            stu_num = stu_num+"00"+saveduser.getId();
        }
        saveduser.setEmail(stu_num+"@stu.kln.ac.lk");
        saveduser.setUserId(stu_num);
        saveduser.setPassword(encoder.encode(saveduser.getEmail()));
        saveduser = userRepo.save(saveduser);

        AddStudentResponse addStudentResponse = new AddStudentResponse();
        addStudentResponse.setEmail(saveduser.getEmail());
        addStudentResponse.setUser_id(saveduser.getUserId());

        return addStudentResponse;
    }

    public String addCourse(CourseRequest courseRequest){
        String c_code = courseRequest.getYear()
                + courseRequest.getSemester()
                + "0"
                + ((courseRequest.getSubject().length() <= 1) ? "0" : "")
                + courseRequest.getSubject();

        if(coursesRepo.existsByCourseCode(c_code)){
            throw new RuntimeException("Course already exists with this code :"+c_code);
        }
        Course course = new Course();
        course.setCourseCode(c_code);
        course.setTitle(courseRequest.getCourse_title());
        course.setDescription(courseRequest.getCourse_description());
        coursesRepo.save(course);
        return course.getCourseCode();
    }

    public void addResults(ResultsRequest result) {
        System.out.println(result.getUserId());
        Optional<User> user = userRepo.findByUserId(String.valueOf(result.getUserId()));
        if(user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<Course> course = coursesRepo.findByCourseCode(result.getCourseCode());
        if (course.isEmpty()) {
            throw new RuntimeException("Course not found");
        }
        Optional<Enrollment> enrollment = enrollmentRepo.findByUserAndCourse(user.get(), course.get());
        if (enrollment.isEmpty()) {
            throw new RuntimeException("Student Does not enrolled in this course");
        }
        enrollment.get().setGrade(result.getGrade());
        enrollmentRepo.save(enrollment.get());

    }

}
