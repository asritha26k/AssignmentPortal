package com.example.schoolportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // <-- Ensure this import is present
// import com.fasterxml.jackson.annotation.JsonIgnore; // This import is not needed if you use @JsonBackReference
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference("student-submissions") // <--- ENSURE THIS IS EXACTLY HERE AND NAME MATCHES Student
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    @JsonBackReference("assignment-submissions") // <--- ENSURE THIS IS EXACTLY HERE AND HAS THIS NAME
    private Assignment assignment;

    @ElementCollection
    private List<String> photoUrls;
    private Integer marks;
    private String feedback;
    @CreationTimestamp
    private LocalDateTime submittedDate;
}