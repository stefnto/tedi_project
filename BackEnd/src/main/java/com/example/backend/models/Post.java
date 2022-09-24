package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    @OneToMany
    private Collection<Comment> comments = new ArrayList<>();
    private Date date;
    private String post_name;
    private String post_surname;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Image> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Video> videos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Audio> audios = new ArrayList<>();
}
