package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExperienceDTO {
    Boolean isPublic;
    String text;
}
