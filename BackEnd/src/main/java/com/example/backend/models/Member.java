package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = true, unique = true)
    private String email;
    private String name;
    private String surname;
    private String password;
    private String phone;

    // posts the user has made
    @OneToMany(fetch = FetchType.LAZY)
//    @JsonIgnore
    private Collection<Post> posts = new ArrayList<>();

    //ads that the user has made
    @OneToMany(fetch = FetchType.LAZY)
//    @JsonIgnore
    private Collection<Ad> ads = new ArrayList<>();


    // load the roles everytime we load a user
    @ManyToMany(fetch = FetchType.LAZY)
//    @JsonIgnore
    private Collection<Role> roles = new ArrayList<>();


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id", unique = true)
//    @JsonIgnore
    private Resume resume;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id", referencedColumnName = "id", unique = true)
//    @JsonIgnore
    private Education education;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skills_id", referencedColumnName = "id", unique = true)
//    @JsonIgnore
    private Skills skills;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id", referencedColumnName = "id", unique = true)
//    @JsonIgnore
    private Experience experience;



    public Member(String surname, String name, String password, String email, String phone, Collection<Role> roles ){
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }
}

