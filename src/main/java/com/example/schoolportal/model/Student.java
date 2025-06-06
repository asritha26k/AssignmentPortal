package com.example.schoolportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- Ensure these imports are present
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference("classroom-students") // <--- ENSURE THIS IS EXACTLY HERE AND NAME MATCHES Classroom
    private Classroom classroom;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("student-submissions") // <--- ENSURE THIS IS EXACTLY HERE AND HAS THIS NAME
    private List<Submission> submissions;

    @Enumerated(EnumType.STRING)
    private Role role;
}