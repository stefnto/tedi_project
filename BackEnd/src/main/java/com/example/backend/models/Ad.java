package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Ad implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    String text;
    Date date;
    String name;
    String surname;
    String prerequisite_skills;
}
