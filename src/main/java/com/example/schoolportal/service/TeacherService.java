package com.example.schoolportal.service;

import com.example.schoolportal.dto.AssignmentDTO;
import com.example.schoolportal.dto.ClassroomDTO;
import com.example.schoolportal.model.*;
import com.example.schoolportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepo;
    private final ClassroomRepository classroomRepo;
    private final AssignmentRepository assignmentRepo;
    private final SubmissionRepository submissionRepo;
    private final StudentRepository studentRepo;

    public Classroom createClassroom(String teacherEmail, ClassroomDTO dto) {
        Teacher teacher = teacherRepo.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Classroom classroom = new Classroom();
        classroom.setName(dto.getName());
        classroom.setTeacher(teacher);

        return classroomRepo.save(classroom);
    }

    public Classroom getClassroomDetails(Long id) {
        return classroomRepo.findById(id).orElseThrow(() -> new RuntimeException("Classroom not found"));
    }

    public Assignment uploadAssignment(Long classroomId, AssignmentDTO dto) {
        Classroom classroom = classroomRepo.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .classroom(classroom)
                .build();

        return assignmentRepo.save(assignment);
    }

    public List<Assignment> getAssignments(Long classroomId) {
        Classroom classroom = classroomRepo.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        return assignmentRepo.findByClassroom(classroom);
    }

    public Submission getSubmission(Long submissionId) {
        return submissionRepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    public Submission evaluateSubmission(Long id, Integer marks, String feedback) {
        Submission submission = submissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setMarks(marks);
        submission.setFeedback(feedback);
        return submissionRepo.save(submission);
    }

    public void addStudentsToClassroom(String teacherEmail, Long classroomId, List<String> studentEmails) {
        Teacher teacher = teacherRepo.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Classroom classroom = classroomRepo.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if (!classroom.getTeacher().getId().equals(teacher.getId())) {
            throw new SecurityException("You are not authorized to add students to this classroom");
        }

        List<Student> students = studentRepo.findByEmailIn(studentEmails);
        for (Student student : students) {
            student.setClassroom(classroom);
        }
        studentRepo.saveAll(students);
    }

    public Classroom updateClassroom(Long id, ClassroomDTO dto) {
        Classroom classroom = classroomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        classroom.setName(dto.getName());
        return classroomRepo.save(classroom);
    }

    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        classroomRepo.delete(classroom);
    }

    public Assignment updateAssignment(Long id, AssignmentDTO dto) {
        Assignment assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        return assignmentRepo.save(assignment);
    }

    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignmentRepo.delete(assignment);
    }
}
