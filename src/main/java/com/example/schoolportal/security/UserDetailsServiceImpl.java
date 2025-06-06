package com.example.schoolportal.security;

import com.example.schoolportal.model.Teacher;
import com.example.schoolportal.model.Student;
import com.example.schoolportal.repository.TeacherRepository;
import com.example.schoolportal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (teacherRepo.findByEmail(email).isPresent()) {
            Teacher t = teacherRepo.findByEmail(email).get();
            return new User(t.getEmail(), t.getPassword(),
                    java.util.List.of(new SimpleGrantedAuthority(t.getRole().name())));
        } else if (studentRepo.findByEmail(email).isPresent()) {
            Student s = studentRepo.findByEmail(email).get();
            return new User(s.getEmail(), s.getPassword(),
                    java.util.List.of(new SimpleGrantedAuthority(s.getRole().name())));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
