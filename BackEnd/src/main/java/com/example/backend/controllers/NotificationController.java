package com.example.backend.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.services.NotificationServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationServiceImpl notificationService;

    // returns list of unseen notifications
    @GetMapping("/get/{email}")
    public ResponseEntity<?> getNotifications(@PathVariable String email){
        return ResponseEntity.ok().body(notificationService.getMemberNotifications(email));
    }

    // marks the notification specified as seen by the member
    @PostMapping("/seen")
    public ResponseEntity<String> seeNotification(@RequestBody Long id){
        return ResponseEntity.status(201).body(notificationService.seenNotification(id));
    }
}
