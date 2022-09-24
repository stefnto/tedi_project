package com.example.backend.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom implements Serializable{

    // each chatroom consists of 2 participants and a chatroom ID

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_one_id", referencedColumnName = "id")
    private Member member_one;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_two_id", referencedColumnName = "id")
    private Member member_two;


}
