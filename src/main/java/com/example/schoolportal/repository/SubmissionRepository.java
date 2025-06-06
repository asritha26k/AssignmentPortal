package com.example.schoolportal.repository;

import com.example.schoolportal.model.Student;
import com.example.schoolportal.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudent(Student student);
}
