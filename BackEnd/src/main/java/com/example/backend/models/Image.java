package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    @Column(name = "content", length = 1000)
    private byte[] content;
    private String name;
    private String type;

    public Image(String name, String type, byte[] content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }
}
