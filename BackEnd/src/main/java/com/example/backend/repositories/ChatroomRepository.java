package com.example.backend.repositories;

import com.example.backend.models.Chatroom;
import com.example.backend.models.Member;
import com.example.backend.models.MessageDTO;
import com.example.backend.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Modifying
    @Query(value = "INSERT INTO messages VALUES (:currDate, :text, :chatroom_id, :member_id)", nativeQuery = true)
    void saveMessage(Date currDate, String text, Long chatroom_id, Long member_id);

    @Query(value = "SELECT mess.*, m.email " +
            "FROM messages mess, member m " +
            "WHERE mess.chatroom_id = :chatroom_id AND mess.member_id = :member_id AND m.id = mess.member_id",
            nativeQuery = true)
    List<Object[]> showMessages(Long chatroom_id, Long member_id);

    @Query(value = "Select chat_room_id " +
            "FROM chatroom " +
            "WHERE (member_one_id = :member_id AND member_two_id = :interlocutor_member_id) " +
            "    OR (member_one_id = :interlocutor_member_id AND member_two_id = :member_id)",
            nativeQuery = true)
    Long getChatroomID(Long member_id, Long interlocutor_member_id);
}
