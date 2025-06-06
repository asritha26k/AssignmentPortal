package com.example.schoolportal.service;

import com.example.schoolportal.model.Assignment;
import com.example.schoolportal.model.Classroom;
import com.example.schoolportal.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepo;

    public Assignment getAssignment(Long id) {
        return assignmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public List<Assignment> getAssignmentsByClassroom(Classroom classroom) {
        return assignmentRepo.findByClassroom(classroom);
    }
}
