package com.example.schoolportal.repository;

import com.example.schoolportal.model.Assignment;
import com.example.schoolportal.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByClassroom(Classroom classroom);
}
