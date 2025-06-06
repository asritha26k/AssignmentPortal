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
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference("teacher-classrooms") // <--- ENSURE THIS IS EXACTLY HERE AND NAME MATCHES Teacher
    private Teacher teacher;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("classroom-students") // <--- ENSURE THIS IS EXACTLY HERE AND HAS THIS NAME
    private List<Student> students;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("classroom-assignment")
    private List<Assignment> assignments;
}