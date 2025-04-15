package com.example.backend.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.models.Chatroom;
import com.example.backend.models.Member;
import com.example.backend.models.MessageDTO;
import com.example.backend.repositories.ChatroomRepository;
import com.example.backend.repositories.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatroomRepository chatRep;
    private final MemberRepository memberRep;

    @Override
    public void saveChatroom(Member member_one, Member member_two) {

        try {
            chatRep.save(new Chatroom(null, member_one, member_two));
        } catch (Exception e) {
            log.error("Error creating chatroom: {}", e.getMessage());
        }
        
    }

    @Override
    public void saveMessage(String text, String email, String interlocutor_email) {

        Member member = memberRep.findByEmail(email);
        Member interlocutor_member = memberRep.findByEmail(interlocutor_email);

        Long chat_id = chatRep.getChatroomID(member.getId(), interlocutor_member.getId());
        chatRep.saveMessage(new Date(), text, chat_id, member.getId());
    }

    @Override
    public List<MessageDTO> showMessagesOfUserInChat(String email, String interlocutor_email) {

        Member member = memberRep.findByEmail(email);
        Member interlocutor_member = memberRep.findByEmail(interlocutor_email);

        Long chat_id = chatRep.getChatroomID(member.getId(), interlocutor_member.getId());
        List<Object[]> list = chatRep.showMessages(chat_id, member.getId());

        List<MessageDTO> messages = new ArrayList<>();
        list.forEach(object -> messages.add(new MessageDTO(object[0].toString(), object[1].toString(), object[4].toString())));
        return messages;
    }
}
