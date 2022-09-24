package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private Date date;
    private String post_name;
    private String post_surname;
    private String text;
}
