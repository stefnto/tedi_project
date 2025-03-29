package com.example.backend.controllers;


import com.example.backend.services.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
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
