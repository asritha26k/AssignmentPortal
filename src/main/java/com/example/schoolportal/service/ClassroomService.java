package com.example.schoolportal.service;

import com.example.schoolportal.model.Classroom;
import com.example.schoolportal.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepo;

    public Classroom getClassroom(Long id) {
        return classroomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
    }
}
