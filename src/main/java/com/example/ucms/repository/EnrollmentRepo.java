package com.example.ucms.repository;

import com.example.ucms.model.Course;
import com.example.ucms.model.Enrollment;
import com.example.ucms.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);


    @Query("SELECT e FROM Enrollment e WHERE e.user.userId = :userId")
    List<Enrollment> findAllCoursesOfUser(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId")
    int deleteCoursesByUser(@Param("userId") String userId, @Param("courseId") String courseId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO enrollment (user_id, course_code) VALUES (:userId, :courseId)", nativeQuery = true)
    int insertCoursesByUser(@Param("userId") String userId, @Param("courseId") String courseId);

}
