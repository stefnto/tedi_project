package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberPersonalDataDTO {
  private ExperienceDTO experience;
  private EducationDTO education;
  private ResumeDTO resume;
  private SkillsDTO skills;
}
