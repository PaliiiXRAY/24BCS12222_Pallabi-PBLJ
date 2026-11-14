-- ==========================================================
-- Student Management System Database Schema (MySQL)
-- ==========================================================

CREATE DATABASE IF NOT EXISTS student_management_db;
USE student_management_db;

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    course VARCHAR(255) NOT NULL,
    age INT NOT NULL CHECK (age > 0)
);

-- Sample Data (Optional)
INSERT INTO students (name, email, course, age) VALUES 
('Alice Johnson', 'alice.johnson@example.com', 'Computer Science', 21),
('Bob Smith', 'bob.smith@example.com', 'Electrical Engineering', 22),
('Charlie Brown', 'charlie.brown@example.com', 'Computer Science', 20),
('Diana Prince', 'diana.prince@example.com', 'Mechanical Engineering', 23)
ON DUPLICATE KEY UPDATE id=id;
