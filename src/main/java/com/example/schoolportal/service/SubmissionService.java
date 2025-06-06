package com.example.schoolportal.service;

import com.example.schoolportal.model.Student;
import com.example.schoolportal.model.Submission;
import com.example.schoolportal.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepo;

    public Submission getSubmission(Long id) {
        return submissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    public List<Submission> getSubmissionsByStudent(Student student) {
        return submissionRepo.findByStudent(student);
    }

    public Submission saveSubmission(Submission submission) {
        return submissionRepo.save(submission);
    }
}
