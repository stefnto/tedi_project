package com.example.backend.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@Slf4j
public class Messages implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroomId", referencedColumnName = "chatRoomId")
    private Chatroom chatroom;

    @Id
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private String text;

    @Id
    private Date dateSent;
}
