package com.example.backend.models;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // date friendship request was send
    @Column(name = "request_creation_date")
    private Date requestCreationDate;


    // date friendship request was accepted
    @Column(name = "request_acceptance_date")
    private Date requestAcceptanceDate;

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
