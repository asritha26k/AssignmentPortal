// src/main/java/com/example/schoolportal/service/AuthService.java

package com.example.schoolportal.service;

import com.example.schoolportal.dto.AuthRequest;
import com.example.schoolportal.dto.RegisterRequest;
import com.example.schoolportal.model.Role;
import com.example.schoolportal.model.Student;
import com.example.schoolportal.model.Teacher;
import com.example.schoolportal.repository.StudentRepository;
import com.example.schoolportal.repository.TeacherRepository;
// import com.example.schoolportal.security.JwtUtil; // REMOVE this import, JwtUtil is no longer directly used here
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority; // REMOVE this import if no longer needed
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    // private final JwtUtil jwtUtil; // REMOVE this field

    public void registerTeacher(RegisterRequest request) {
        if (teacherRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setEmail(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setRole(Role.ROLE_TEACHER);
        teacherRepo.save(teacher);
    }

    public void registerStudent(RegisterRequest request) {
        if (studentRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.ROLE_STUDENT);
        studentRepo.save(student);
    }

    // --- MODIFIED authenticate method ---
    // Now returns the Authentication object, not the JWT string
    public Authentication authenticate(AuthRequest request) {
        // This will authenticate the user and return a fully populated Authentication object
        // if credentials are valid. Otherwise, it will throw an AuthenticationException.
        return authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    }
}