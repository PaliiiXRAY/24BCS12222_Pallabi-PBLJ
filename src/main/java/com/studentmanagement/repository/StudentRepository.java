package com.studentmanagement.repository;

import com.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Retrieve students by course (case-insensitive).
     *
     * @param course Enrolled course name
     * @return List of matching students
     */
    List<Student> findByCourseIgnoreCase(String course);

    /**
     * Retrieve students by exact course name.
     *
     * @param course Enrolled course name
     * @return List of matching students
     */
    List<Student> findByCourse(String course);
}
