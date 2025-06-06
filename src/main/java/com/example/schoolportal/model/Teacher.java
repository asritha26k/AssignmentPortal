package com.example.schoolportal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- Ensure this import is present
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("teacher-classrooms") // <--- ENSURE THIS IS EXACTLY HERE
    private List<Classroom> classrooms;

    @Enumerated(EnumType.STRING)
    private Role role;
}