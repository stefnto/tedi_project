package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SkillsDTO {
    Boolean isPublic;
    String text;
}