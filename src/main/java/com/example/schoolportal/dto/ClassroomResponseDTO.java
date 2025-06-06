// src/main/java/com/example/schoolportal/dto/response/ClassroomResponseDTO.java
package com.example.schoolportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomResponseDTO {
    private Long id;
    private String name;
    // You might want to include summary info for the teacher, not the full Teacher entity
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    // You could add counts if needed, e.g., private int studentCount;
}