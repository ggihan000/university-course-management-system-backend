package com.example.ucms.service;

import com.example.ucms.dto.GetCourseDetailResponse;
import com.example.ucms.dto.ResponseCourse;
import com.example.ucms.model.Course;
import com.example.ucms.model.Enrollment;
import com.example.ucms.model.User;
import com.example.ucms.repository.CoursesRepo;
import com.example.ucms.repository.EnrollmentRepo;
import com.example.ucms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class StudentService {

    @Autowired
    private CoursesRepo coursesRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private UserRepo userRepo;

    public void enrollCourse(String course_id, String student_id){
        System.out.println(course_id+" "+student_id);
        Optional<User> user = userRepo.findByUserId(student_id);
        Optional<Course> course = coursesRepo.findByCourseCode(course_id);
        if(course.isEmpty() || user.isEmpty()){
            throw new RuntimeException("Course or User not found");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course.get());
        enrollment.setUser(user.get());

        enrollmentRepo.save(enrollment);
    }

    public void unEnrollCourse(String course_id, String student_id){
        Optional<User> user = userRepo.findByUserId(student_id);
        Optional<Course> course = coursesRepo.findByCourseCode(course_id);
        if(course.isEmpty() || user.isEmpty()){
            throw new RuntimeException("Course or User not found");
        }
        Optional<Enrollment> enrollment = enrollmentRepo.findByUserAndCourse(user.get(),course.get());
        if (enrollment.isEmpty()){
            throw new RuntimeException("User not enrolled with "+course_id+" course");
        }
        enrollmentRepo.delete(enrollment.get());
    }

    public GetCourseDetailResponse getCourseDetails(String userId){
        List<Course> courses = coursesRepo.findAll();
        List<Enrollment> enrollments = enrollmentRepo.findAllCoursesOfUser(userId);

        for (Enrollment e : enrollments) {
            System.out.println(e.getCourse().getCourseCode());
        }

        for (Course c : courses) {
            System.out.println(c.getCourseCode());
        }

        List<ResponseCourse> unEnrolledCourses = new ArrayList<>();
        List<ResponseCourse> enrolledCourses = new ArrayList<>();

        Set<String> enrolledCourseCodes = new HashSet<>();
        for (Enrollment e : enrollments) {
            enrolledCourseCodes.add(e.getCourse().getCourseCode());

            ResponseCourse responseCourse = new ResponseCourse();
            responseCourse.setCourseCode(e.getCourse().getCourseCode());
            responseCourse.setCourseDescription(e.getCourse().getDescription());
            responseCourse.setCourseName(e.getCourse().getTitle());
            responseCourse.setGrade(e.getGrade());
            enrolledCourses.add(responseCourse);
        }

        Iterator<Course> iterator = courses.iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();


            if (!enrolledCourseCodes.contains(c.getCourseCode())) {
                ResponseCourse responseCourse = new ResponseCourse();
                responseCourse.setCourseCode(c.getCourseCode());
                responseCourse.setCourseDescription(c.getDescription());
                responseCourse.setCourseName(c.getTitle());
                unEnrolledCourses.add(responseCourse);
            }
        }

        GetCourseDetailResponse getCourseDetailResponse = new GetCourseDetailResponse();
        getCourseDetailResponse.setEnrolledCourses(enrolledCourses);
        getCourseDetailResponse.setUnEnrolledCourses(unEnrolledCourses);

        return getCourseDetailResponse;
    }

}
