package com.example.schoolportal.controller;

import com.example.schoolportal.exception.ResourceNotFoundException;
import com.example.schoolportal.model.Assignment;
import com.example.schoolportal.model.Classroom;
import com.example.schoolportal.model.Student;
import com.example.schoolportal.model.Submission;
import com.example.schoolportal.repository.AssignmentRepository;
import com.example.schoolportal.repository.ClassroomRepository;
import com.example.schoolportal.repository.StudentRepository;
import com.example.schoolportal.repository.SubmissionRepository;
import com.example.schoolportal.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/classroom")
    public ResponseEntity<Classroom> getAssignedClassroom(Authentication authentication) {
        return ResponseEntity.ok(studentService.getAssignedClassroom(authentication.getName()));
    }

    @GetMapping("/classroom/assignments")
    public ResponseEntity<List<Assignment>> getAssignments(Authentication authentication) {
        return ResponseEntity.ok(studentService.getAssignments(authentication.getName()));
    }

    @PostMapping(value = "/assignment/{id}/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Submission> submitAssignment(@PathVariable Long id,
                                                       @RequestPart("files") List<MultipartFile> files,
                                                       Authentication authentication) throws IOException {
        return ResponseEntity.ok(studentService.submitAssignment(id, files, authentication.getName()));
    }

    @GetMapping("/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(Authentication authentication) {
        return ResponseEntity.ok(studentService.getSubmissions(authentication.getName()));
    }

    @PutMapping(value = "/submission/{id}/resubmit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Submission> updateSubmission(@PathVariable Long id,
                                                       @RequestPart("files") List<MultipartFile> files,
                                                       Authentication authentication) throws IOException {
        return ResponseEntity.ok(studentService.resubmitAssignment(id, files, authentication.getName()));
    }

    @DeleteMapping("/submission/{id}")
    public ResponseEntity<String> deleteSubmission(@PathVariable Long id, Authentication authentication) {
        studentService.deleteSubmission(id, authentication.getName());
        return ResponseEntity.ok("Submission deleted");
    }
}
