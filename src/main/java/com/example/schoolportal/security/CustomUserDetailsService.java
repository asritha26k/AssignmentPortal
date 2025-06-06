package com.example.schoolportal.security;

import com.example.schoolportal.model.Student;
import com.example.schoolportal.model.Teacher;
import com.example.schoolportal.repository.StudentRepository;
import com.example.schoolportal.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try teacher first
        if (teacherRepo.findByEmail(email).isPresent()) {
            Teacher teacher = teacherRepo.findByEmail(email).get();
            return new User(
                    teacher.getEmail(),
                    teacher.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority( teacher.getRole().name())) // Add ROLE_ prefix here
            );
        }

        // Then student
        if (studentRepo.findByEmail(email).isPresent()) {
            Student student = studentRepo.findByEmail(email).get();
            return new User(
                    student.getEmail(),
                    student.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority( student.getRole().name())) // Add ROLE_ prefix here
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}