# Student Management REST API

[![Java CI with Maven](https://github.com/PaliiiXRAY/24BCS12222_Pallabi-PBLJ/actions/workflows/ci.yml/badge.svg)](https://github.com/PaliiiXRAY/24BCS12222_Pallabi-PBLJ/actions/workflows/ci.yml)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.2-brightgreen)
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![MySQL](https://img.shields.io/badge/Database-MySQL_8.0-blue)

A robust RESTful API built with **Spring Boot**, **Spring Data JPA**, and **MySQL** for managing student records as specified in the assignment.

---

## 🚀 Features

- **CRUD Operations**: Add, retrieve, update, and delete student records.
- **Custom Search Endpoint**: Filter and retrieve students by course (case-insensitive).
- **Spring Data JPA**: Clean repository pattern with Hibernate ORM.
- **Robust Exception Handling**: Global exception handling with `@RestControllerAdvice` returning standard HTTP status codes (200, 201, 400, 404, 500).
- **Bean Validation**: Input validation with Hibernate Validator (`@NotBlank`, `@Email`, `@Min`, `@NotNull`).
- **Full Test Coverage**: 23 automated unit and integration tests covering repository, service, and controller layers with 100% passing rate.
- **Postman Collection Included**: Ready-to-import Postman JSON collection for testing all endpoints.

---

## 🛠 Tech Stack

- **Java**: 17+ (tested with OpenJDK 25)
- **Framework**: Spring Boot
- **ORM / Persistence**: Spring Data JPA / Hibernate
- **Database**: MySQL 8.0+ (with H2 in-memory profile for testing)
- **Validation**: Jakarta Validation / Hibernate Validator
- **Build Tool**: Apache Maven (via Maven Wrapper `mvnw`)
- **API Testing**: Postman & JUnit 5 / MockMvc

---

## 📋 Entity Requirements

The `Student` entity represents a student record stored in the `students` table:

| Attribute | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | Primary Key, Auto-generated (`IDENTITY`) | Unique identifier for each student |
| `name` | `String` | Non-blank, Not null | Full name of the student |
| `email` | `String` | Non-blank, Valid Email, Unique | Student email address |
| `course` | `String` | Non-blank, Not null | Enrolled course name |
| `age` | `Integer` | Not null, Min = 1 | Student age |

---

## 🌐 REST API Endpoints

Base URL: `http://localhost:8080`

| HTTP Method | Endpoint | Description | Success Status |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/students` | Add a new student | `201 Created` |
| **GET** | `/api/students` | Retrieve all students | `200 OK` |
| **GET** | `/api/students/{id}` | Retrieve student by ID | `200 OK` (or `404 Not Found`) |
| **PUT** | `/api/students/{id}` | Update student details | `200 OK` (or `404 Not Found`) |
| **DELETE** | `/api/students/{id}` | Delete a student | `200 OK` (or `404 Not Found`) |
| **GET** | `/api/students/course/{course}` | Retrieve students by course | `200 OK` |

---

## 📦 Request & Response Examples

### 1. Add a New Student
- **Method**: `POST`
- **URL**: `/api/students`
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```
- **Response** (`201 Created`):
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```

### 2. Retrieve All Students
- **Method**: `GET`
- **URL**: `/api/students`
- **Response** (`200 OK`):
  ```json
  [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "course": "Computer Science",
      "age": 21
    }
  ]
  ```

### 3. Retrieve Student by ID
- **Method**: `GET`
- **URL**: `/api/students/1`
- **Response** (`200 OK`):
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```

### 4. Update Student Details
- **Method**: `PUT`
- **URL**: `/api/students/1`
- **Request Body**:
  ```json
  {
    "name": "Johnathan Doe",
    "email": "johnathan.doe@example.com",
    "course": "Artificial Intelligence",
    "age": 22
  }
  ```
- **Response** (`200 OK`):
  ```json
  {
    "id": 1,
    "name": "Johnathan Doe",
    "email": "johnathan.doe@example.com",
    "course": "Artificial Intelligence",
    "age": 22
  }
  ```

### 5. Retrieve Students by Course
- **Method**: `GET`
- **URL**: `/api/students/course/Computer Science`
- **Response** (`200 OK`):
  ```json
  [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "course": "Computer Science",
      "age": 21
    }
  ]
  ```

### 6. Delete a Student
- **Method**: `DELETE`
- **URL**: `/api/students/1`
- **Response** (`200 OK`):
  ```json
  {
    "success": true,
    "message": "Student deleted successfully with id: 1"
  }
  ```

### 7. Error Handling (404 Not Found)
- **URL**: `/api/students/999`
- **Response** (`404 Not Found`):
  ```json
  {
    "timestamp": "2026-11-14T11:10:00",
    "status": 404,
    "error": "Not Found",
    "message": "Student not found with id: 999",
    "path": "/api/students/999"
  }
  ```

### 8. Validation Error Handling (400 Bad Request)
- **URL**: `/api/students` (with invalid fields)
- **Response** (`400 Bad Request`):
  ```json
  {
    "timestamp": "2026-11-14T11:10:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed for one or more fields",
    "path": "/api/students",
    "validationErrors": {
      "name": "Student name is required",
      "email": "Please provide a valid email address",
      "course": "Enrolled course is required",
      "age": "Student age must be greater than 0"
    }
  }
  ```

---

## 🗄 Database Configuration

Configured in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> **Tip**: You can override the credentials using environment variables `DB_USERNAME` and `DB_PASSWORD`, or edit `application.properties` directly with your MySQL root password.

An optional SQL setup script is provided in `src/main/resources/schema.sql`.

---

## 🧪 Running the Tests

To run the complete automated test suite (23 tests):

```bash
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

---

## 🚀 Running the Application

To run the Spring Boot application locally:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

The application will start on port `8080`.

---

## 📬 Postman Testing

A pre-configured Postman collection is included in the project:
`postman/Student_Management_API.postman_collection.json`

### How to use:
1. Open **Postman**.
2. Click **Import** in the top left corner.
3. Select or drag-and-drop `postman/Student_Management_API.postman_collection.json`.
4. Ensure the application is running (`http://localhost:8080`).
5. Execute requests 1 through 8 in sequence to test and capture screenshots!

---

## 📤 Pushing to GitHub

To push this repository to GitHub:

1. Create a new repository on [GitHub](https://github.com/new) (e.g., `student-management-system`).
2. Run the following commands in the terminal from this project root:

```bash
git remote add origin https://github.com/<your-username>/student-management-system.git
git branch -M main
git push -u origin main
```
