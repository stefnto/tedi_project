package com.example.backend.services;

import java.util.List;

import com.example.backend.models.MemberInfo;

public interface FriendService {

    /**
     * @return true or false depending if a friendship exists between the two members
     */
    Boolean friendshipRequestExists(Long firstMemberId, Long SecondMemberId);

    /**
     * @return true or false depending if members are friends
     * */
    Boolean friendshipRequestIsAccepted(String firstMemberEmail, String secondMemberEmail);

    /** 
     * Sends friendshipRequest from firstMemberEmail to secondMemberEmail 
     * @return true or false depending if the request was sent successfully
     */ 
    String sendFriendshipRequest(String firstMemberEmail, String secondMemberEmail);

    /** 
     * Accepts friendshipRequest of firstMemberEmail to secondMemberEmail
     * @return true or false depending if the request was accepted successfully
     */
    String acceptFriendshipRequest(String firstMemberEmail, String secondMemberEmail);

    /** 
     * Declines friendshipRequest of firstMemberEmail to secondMemberEmail
     * @return true or false depending if the request was declined successfully
     */
    String declineFriendshipRequest(String firstMemberEmail, String secondMemberEmail);

    /**
     * @return List of MemberInfo objects representing the friends of the member.
     */
    List<MemberInfo> getFriends(String memberEmail);

    /**
     * @return List of String objects representing the unaccepted friendship requests sent to the member.
     */
    List<String> getFriendshipRequests(String memberEmail);
}
