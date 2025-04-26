package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EducationDTO {
    Boolean isPublic;
    String text;
}
