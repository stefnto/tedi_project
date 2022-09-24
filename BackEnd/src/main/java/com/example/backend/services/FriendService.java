package com.example.backend.services;

import com.example.backend.models.MemberInfo;

import java.util.List;

public interface FriendService {

    // sends friendRequest from sender_email to acceptor_email
    String friendRequest(String sender_email, String acceptor_email);

    // accepts friendRequest of sender_email to acceptor_email
    String acceptFriendRequest(String sender_email, String acceptor_email);

    // declines friendRequest of sender_email to acceptor_email
    String declineFriendRequest(String sender_email, String acceptor_email);

    // gets all friends of member_email and returns them
    List<MemberInfo> getFriends(String member_email);

    // returns true or false depending if members are friends
    Boolean areFriends(String email1, String email2);

    // returns list of emails that have sent a friend request to member_email
    List<String> getFriendsRequests(String member_email);
}
