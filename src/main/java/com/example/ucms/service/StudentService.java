package com.example.ucms.service;

import com.example.ucms.dto.GetCourseDetailResponse;
import com.example.ucms.dto.ResponseCourse;
import com.example.ucms.model.Course;
import com.example.ucms.model.Enrollment;
import com.example.ucms.repository.CoursesRepo;
import com.example.ucms.repository.EnrollmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class StudentService {

    @Autowired
    private CoursesRepo coursesRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    public void enrollCourse(String course_id, String student_id){
        int num = enrollmentRepo.insertCoursesByUser(student_id, course_id);
        if(num < 1){
            throw new RuntimeException("Course not enrolled");
        }
    }

    public void unEnrollCourse(String course_id, String student_id){
        int num = enrollmentRepo.deleteCoursesByUser(student_id, course_id);
        if(num < 1){
            throw new RuntimeException("Course not unenrolled");
        }
    }

    public GetCourseDetailResponse getCourseDetails(String userId){
        List<Course> courses = coursesRepo.findAll();
        List<Enrollment> enrollments = enrollmentRepo.findAllCoursesOfUser(userId);

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
