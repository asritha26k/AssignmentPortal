package com.example.schoolportal.dto;

import lombok.Data;

@Data
public class AssignmentDTO {
    private String title;
    private String description;
    // uploadDate, classroom, and submissions are handled internally
}
