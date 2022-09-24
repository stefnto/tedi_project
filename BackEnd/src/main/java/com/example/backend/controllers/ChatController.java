package com.example.backend.controllers;

import com.example.backend.services.ChatroomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https:localhost:4200")
@RequestMapping("/api/chatroom")
public class ChatController {
    private final ChatroomServiceImpl chatroomService;

    // get messages of chat between two members
    @GetMapping("/getMessages/{email}")
    public ResponseEntity<?>getMessages(@PathVariable("email") String email, @RequestHeader String interlocutor){
        return ResponseEntity.ok().body(chatroomService.showMessagesOfUserInChat(email, interlocutor));
    }

    // send message
    @PostMapping("/sendMessage")
    public ResponseEntity<?>sendMessages(@RequestBody String text, @RequestHeader String email,
                                         @RequestHeader String interlocutor){
        chatroomService.saveMessage(text, email, interlocutor);
        return ResponseEntity.status(201).body("Message sent");
    }
}
