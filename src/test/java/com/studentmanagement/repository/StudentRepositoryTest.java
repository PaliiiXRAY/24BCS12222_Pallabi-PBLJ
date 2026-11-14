package com.studentmanagement.repository;

import com.studentmanagement.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student sampleStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        sampleStudent = new Student("Alice Johnson", "alice@example.com", "Computer Science", 20);
    }

    @Test
    @DisplayName("Test save student")
    void testSaveStudent() {
        Student saved = studentRepository.save(sampleStudent);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alice Johnson");
        assertThat(saved.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("Test find student by ID")
    void testFindById() {
        Student saved = studentRepository.save(sampleStudent);
        Optional<Student> found = studentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice Johnson");
    }

    @Test
    @DisplayName("Test find students by course (case-insensitive)")
    void testFindByCourseIgnoreCase() {
        studentRepository.save(sampleStudent);
        studentRepository.save(new Student("Bob Smith", "bob@example.com", "computer science", 22));
        studentRepository.save(new Student("Charlie Brown", "charlie@example.com", "Mechanical", 21));

        List<Student> csStudents = studentRepository.findByCourseIgnoreCase("COMPUTER SCIENCE");

        assertThat(csStudents).hasSize(2);
        assertThat(csStudents).extracting(Student::getName).containsExactlyInAnyOrder("Alice Johnson", "Bob Smith");
    }

    @Test
    @DisplayName("Test delete student")
    void testDeleteStudent() {
        Student saved = studentRepository.save(sampleStudent);
        studentRepository.deleteById(saved.getId());

        Optional<Student> found = studentRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
