package com.studentmanagement.service;

import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "Alice Johnson", "alice@example.com", "Computer Science", 20);
    }

    @Test
    @DisplayName("Create student successfully")
    void testCreateStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student created = studentService.createStudent(new Student("Alice Johnson", "alice@example.com", "Computer Science", 20));

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Get all students")
    void testGetAllStudents() {
        Student student2 = new Student(2L, "Bob Smith", "bob@example.com", "Physics", 22);
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student, student2));

        List<Student> students = studentService.getAllStudents();

        assertThat(students).hasSize(2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get student by ID - Found")
    void testGetStudentByIdFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student found = studentService.getStudentById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alice Johnson");
    }

    @Test
    @DisplayName("Get student by ID - Not Found Throws Exception")
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: 99");
    }

    @Test
    @DisplayName("Update student details successfully")
    void testUpdateStudent() {
        Student updateInfo = new Student("Alice Cooper", "alice.new@example.com", "Data Science", 21);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = studentService.updateStudent(1L, updateInfo);

        assertThat(updated.getName()).isEqualTo("Alice Cooper");
        assertThat(updated.getEmail()).isEqualTo("alice.new@example.com");
        assertThat(updated.getCourse()).isEqualTo("Data Science");
        assertThat(updated.getAge()).isEqualTo(21);
    }

    @Test
    @DisplayName("Update non-existing student throws ResourceNotFoundException")
    void testUpdateStudentNotFound() {
        Student updateInfo = new Student("Ghost", "ghost@example.com", "Math", 25);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudent(99L, updateInfo))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: 99");
    }

    @Test
    @DisplayName("Delete student successfully")
    void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    @DisplayName("Delete non-existing student throws ResourceNotFoundException")
    void testDeleteStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: 99");
    }

    @Test
    @DisplayName("Get students by course")
    void testGetStudentsByCourse() {
        when(studentRepository.findByCourseIgnoreCase("Computer Science")).thenReturn(List.of(student));

        List<Student> students = studentService.getStudentsByCourse("Computer Science");

        assertThat(students).hasSize(1);
        assertThat(students.get(0).getCourse()).isEqualTo("Computer Science");
    }
}
