package com.example.schoolportal.controller;

import com.example.schoolportal.dto.AssignmentDTO;
import com.example.schoolportal.dto.ClassroomDTO;
import com.example.schoolportal.exception.ResourceNotFoundException;
import com.example.schoolportal.model.Assignment;
import com.example.schoolportal.model.Classroom;
import com.example.schoolportal.model.Submission;
import com.example.schoolportal.model.Teacher;
import com.example.schoolportal.repository.AssignmentRepository;
import com.example.schoolportal.repository.ClassroomRepository;
import com.example.schoolportal.repository.SubmissionRepository;
import com.example.schoolportal.repository.TeacherRepository;
import com.example.schoolportal.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/classroom")
    public ResponseEntity<Classroom> createClassroom(Authentication authentication,
                                                     @RequestBody ClassroomDTO dto) {
        return ResponseEntity.ok(teacherService.createClassroom(authentication.getName(), dto));
    }

    @GetMapping("/classroom/{id}")
    public ResponseEntity<Classroom> getClassroomDetails(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getClassroomDetails(id));
    }

    @PostMapping("/classroom/{id}/assignment")
    public ResponseEntity<Assignment> uploadAssignment(@PathVariable Long id,
                                                       @RequestBody AssignmentDTO dto) {
        return ResponseEntity.ok(teacherService.uploadAssignment(id, dto));
    }

    @GetMapping("/classroom/{id}/assignments")
    public ResponseEntity<List<Assignment>> getAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getAssignments(id));
    }

    @GetMapping("/submission/{id}")
    public ResponseEntity<Submission> getSubmission(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getSubmission(id));
    }

    @PostMapping("/submission/{id}/evaluate")
    public ResponseEntity<Submission> evaluateSubmission(@PathVariable Long id,
                                                         @RequestParam Integer marks,
                                                         @RequestParam String feedback) {
        return ResponseEntity.ok(teacherService.evaluateSubmission(id, marks, feedback));
    }

    @PostMapping("/classroom/{classroomId}/add-students")
    public ResponseEntity<String> addStudentsToClassroom(@PathVariable Long classroomId,
                                                         @RequestBody List<String> studentEmails,
                                                         Authentication authentication) {
        teacherService.addStudentsToClassroom(authentication.getName(), classroomId, studentEmails);
        return ResponseEntity.ok("Students added to classroom");
    }

    @PutMapping("/classroom/{id}")
    public ResponseEntity<Classroom> updateClassroom(@PathVariable Long id,
                                                     @RequestBody ClassroomDTO dto) {
        return ResponseEntity.ok(teacherService.updateClassroom(id, dto));
    }

    @DeleteMapping("/classroom/{id}")
    public ResponseEntity<String> deleteClassroom(@PathVariable Long id) {
        teacherService.deleteClassroom(id);
        return ResponseEntity.ok("Classroom deleted");
    }

    @PutMapping("/assignment/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id,
                                                       @RequestBody AssignmentDTO dto) {
        return ResponseEntity.ok(teacherService.updateAssignment(id, dto));
    }

    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long id) {
        teacherService.deleteAssignment(id);
        return ResponseEntity.ok("Assignment deleted");
    }
}
