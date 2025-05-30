package com.example.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.services.FriendServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendServiceImpl friendService;

    // send friend request from sender to acceptor
    @PostMapping("/request/{email}")
    public ResponseEntity<String> sendFriendRequest(@RequestBody String sender, @PathVariable("email") String acceptor){
        return ResponseEntity.status(201).body(friendService.sendFriendshipRequest(sender, acceptor));
    }

    // accept friend request sent from sender to acceptor
    @PostMapping("/request/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestHeader String sender, @RequestBody String acceptor){
        return ResponseEntity.status(201).body(friendService.acceptFriendshipRequest(sender, acceptor));
    }

    @PostMapping("/request/decline")
    public ResponseEntity<?> declineFriendRequest(@RequestHeader String sender, @RequestBody String acceptor){
        return ResponseEntity.status(201).body(friendService.declineFriendshipRequest(sender, acceptor));
    }

    // check if the two members are friends
    @GetMapping("/request/exists")
    public ResponseEntity<Boolean> checkFriends(@RequestHeader String firstMemberEmail, @RequestHeader String secondMemberEmail){
        return ResponseEntity.ok().body(friendService.friendshipRequestIsAccepted(firstMemberEmail, secondMemberEmail));
    }

    // get list of friends of member
    @GetMapping("/get/{email}")
    public ResponseEntity<?> getListOfFriends(@PathVariable String email){
        return ResponseEntity.ok().body(friendService.getFriends(email));
    }

    // get unaccepted friend requests sent to member
    @GetMapping("/get/requests/{email}")
    public ResponseEntity<List<String>> getFriendRequests(@PathVariable String email){
        return ResponseEntity.ok().body(friendService.getFriendshipRequests(email));
    }

}
