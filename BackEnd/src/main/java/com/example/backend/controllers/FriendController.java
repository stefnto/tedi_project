package com.example.backend.controllers;

import com.example.backend.services.FriendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendServiceImpl friendService;

    // send friend request from sender to acceptor
    @PostMapping("/request/{email}")
    public ResponseEntity<String> sendFriendRequest(@RequestBody String sender, @PathVariable("email") String acceptor){
        return ResponseEntity.status(201).body(friendService.friendRequest(sender, acceptor));
    }

    // accept friend request sent from sender to acceptor
    @PostMapping("/request/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestHeader String sender, @RequestBody String acceptor){
        return ResponseEntity.status(201).body(friendService.acceptFriendRequest(sender, acceptor));
    }

    @PostMapping("/request/decline")
    public ResponseEntity<?> declineFriendRequest(@RequestHeader String sender, @RequestBody String acceptor){
        return ResponseEntity.status(201).body(friendService.declineFriendRequest(sender, acceptor));
    }

    // check if the two members are friends
    @GetMapping("/request/exists")
    public ResponseEntity<Boolean> checkFriends(@RequestHeader String email1, @RequestHeader String email2){
        return ResponseEntity.ok().body(friendService.areFriends(email1, email2));
    }

    // get list of friends of member
    @GetMapping("/get/{email}")
    public ResponseEntity<?> getListOfFriends(@PathVariable("email") String email){
        return ResponseEntity.ok().body(friendService.getFriends(email));
    }

    // get unaccepted friend requests sent to member
    @GetMapping("/get/requests/{email}")
    public ResponseEntity<List<String>> getFriendRequests(@PathVariable("email") String email){
        return ResponseEntity.ok().body(friendService.getFriendsRequests(email));
    }

}
