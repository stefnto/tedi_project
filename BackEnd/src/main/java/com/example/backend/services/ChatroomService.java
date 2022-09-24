package com.example.backend.services;

import com.example.backend.models.Member;
import com.example.backend.models.MessageDTO;

import java.util.List;

public interface ChatroomService {

    // chatroom is made when the two members become friends
    void saveChatroom(Member member_one, Member member_two);


    void saveMessage(String text, String email, String interlocutor_email);

    // returns list of messages of member with "email" on chat with member with "interlocutor_email"
    List<MessageDTO> showMessagesOfUserInChat(String email, String interlocutor_email);
}
