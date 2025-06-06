package com.example.schoolportal.repository;

import com.example.schoolportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    List<Student> findAllByEmailIn(List<String> studentEmails);

    List<Student> findByEmailIn(List<String> studentEmails);
}
