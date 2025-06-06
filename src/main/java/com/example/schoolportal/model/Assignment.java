package com.example.schoolportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- Ensure this import is present
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
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @CreationTimestamp
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference("classroom-assignment")
    private Classroom classroom;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("assignment-submissions") // <--- ENSURE THIS IS EXACTLY HERE
    private List<Submission> submissions;
}