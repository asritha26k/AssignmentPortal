package com.example.schoolportal.service;

import com.example.schoolportal.model.Assignment;
import com.example.schoolportal.model.Classroom;
import com.example.schoolportal.model.Student;
import com.example.schoolportal.model.Submission;
import com.example.schoolportal.repository.AssignmentRepository;
import com.example.schoolportal.repository.StudentRepository;
import com.example.schoolportal.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepo;
    private final SubmissionRepository submissionRepo;
    private final AssignmentRepository assignmentRepo;

    public Classroom getAssignedClassroom(String email) {
        Student student = studentRepo.findByEmail(email).orElseThrow();
        return student.getClassroom();
    }

    public List<Assignment> getAssignments(String email) {
        Student student = studentRepo.findByEmail(email).orElseThrow();
        return assignmentRepo.findByClassroom(student.getClassroom());
    }

    public Submission submitAssignment(Long id, List<MultipartFile> files, String email) throws IOException {
        Student student = studentRepo.findByEmail(email).orElseThrow();
        Assignment assignment = assignmentRepo.findById(id).orElseThrow();

        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("uploads/" + fileName);
            Files.write(filePath, file.getBytes());
            filePaths.add(filePath.toString());
        }

        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .photoUrls(filePaths)
                .build();

        return submissionRepo.save(submission);
    }

    public List<Submission> getSubmissions(String email) {
        Student student = studentRepo.findByEmail(email).orElseThrow();
        return submissionRepo.findByStudent(student);
    }

    public Submission resubmitAssignment(Long id, List<MultipartFile> files, String email) throws IOException {
        Student student = studentRepo.findByEmail(email).orElseThrow();
        Submission submission = submissionRepo.findById(id).orElseThrow(() -> new RuntimeException("Submission not found"));

        if (!submission.getStudent().getId().equals(student.getId())) {
            throw new SecurityException("Not authorized");
        }

        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("uploads/" + fileName);
            Files.write(filePath, file.getBytes());
            filePaths.add(filePath.toString());
        }

        submission.setPhotoUrls(filePaths);
        return submissionRepo.save(submission);
    }

    public void deleteSubmission(Long id, String email) {
        Student student = studentRepo.findByEmail(email).orElseThrow();
        Submission submission = submissionRepo.findById(id).orElseThrow(() -> new RuntimeException("Submission not found"));

        if (!submission.getStudent().getId().equals(student.getId())) {
            throw new SecurityException("Not authorized");
        }

        submissionRepo.delete(submission);
    }
}
