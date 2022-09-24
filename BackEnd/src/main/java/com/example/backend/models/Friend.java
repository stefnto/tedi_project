package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "friend")
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    // each "friendship" has an id number
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // date that "friendship" was made is kept
    @Column(name = "created_date")
    private Date dateCreated;

    // sender of friendship request
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "first_member_id", referencedColumnName = "id")
    private Member firstMember;

    // sender boolean to determine if they have accepted the "friendship"
    private Boolean firstMemberAccepted = false;

    // acceptor of friendship request
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "second_member_id", referencedColumnName = "id")
    private Member secondMember;

    // acceptor boolean to determine if they have accepted the "friendship"
    private Boolean secondMemberAccepted = false;
}
