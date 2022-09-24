package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdDTO {

    private Long member_id;
    private Long id;
    private String text;
    private Date date;
    private String name;
    private String surname;
    private String prerequisite_skills;
}
