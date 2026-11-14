package com.studentmanagement.controller;

import tools.jackson.databind.ObjectMapper;
import com.studentmanagement.entity.Student;
import com.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Student savedStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        Student student = new Student("John Doe", "john.doe@example.com", "Computer Science", 21);
        savedStudent = studentRepository.save(student);
    }

    @Test
    @DisplayName("POST /api/students - Successfully create a student (201 Created)")
    void testCreateStudentSuccess() throws Exception {
        Student newStudent = new Student("Jane Smith", "jane.smith@example.com", "Data Science", 22);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Jane Smith")))
                .andExpect(jsonPath("$.email", is("jane.smith@example.com")))
                .andExpect(jsonPath("$.course", is("Data Science")))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    @DisplayName("POST /api/students - Validation failure (400 Bad Request)")
    void testCreateStudentValidationFailure() throws Exception {
        Student invalidStudent = new Student("", "invalid-email", "", -5);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.name", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.email", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.course", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.age", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/students - Retrieve all students (200 OK)")
    void testGetAllStudents() throws Exception {
        Student anotherStudent = new Student("Alex Ray", "alex@example.com", "Mathematics", 23);
        studentRepository.save(anotherStudent);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("John Doe", "Alex Ray")));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Retrieve student by ID (200 OK)")
    void testGetStudentByIdSuccess() throws Exception {
        mockMvc.perform(get("/api/students/{id}", savedStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedStudent.getId().intValue())))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Student not found (404 Not Found)")
    void testGetStudentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Student not found with id: 99999")));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Update student details (200 OK)")
    void testUpdateStudentSuccess() throws Exception {
        Student updateData = new Student("Johnathan Doe", "johnathan.doe@example.com", "Artificial Intelligence", 22);

        mockMvc.perform(put("/api/students/{id}", savedStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedStudent.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Johnathan Doe")))
                .andExpect(jsonPath("$.email", is("johnathan.doe@example.com")))
                .andExpect(jsonPath("$.course", is("Artificial Intelligence")))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Student not found for update (404 Not Found)")
    void testUpdateStudentNotFound() throws Exception {
        Student updateData = new Student("Ghost", "ghost@example.com", "Physics", 20);

        mockMvc.perform(put("/api/students/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Student not found with id: 99999")));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Delete student (200 OK)")
    void testDeleteStudentSuccess() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", savedStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("Student deleted successfully")));

        mockMvc.perform(get("/api/students/{id}", savedStudent.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Student not found for delete (404 Not Found)")
    void testDeleteStudentNotFound() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Student not found with id: 99999")));
    }

    @Test
    @DisplayName("GET /api/students/course/{course} - Retrieve students by course (200 OK)")
    void testGetStudentsByCourse() throws Exception {
        studentRepository.save(new Student("Sara Connor", "sara@example.com", "Computer Science", 24));
        studentRepository.save(new Student("Kyle Reese", "kyle@example.com", "Mechanical Engineering", 25));

        mockMvc.perform(get("/api/students/course/{course}", "Computer Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("John Doe", "Sara Connor")));
    }
}
