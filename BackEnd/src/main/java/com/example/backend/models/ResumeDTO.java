package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResumeDTO {
    Boolean isPublic;
    String text;
}
