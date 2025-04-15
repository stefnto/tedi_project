package com.example.backend.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.models.Friend;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.repositories.FriendRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRep;
    private final ChatroomService chatService;
    private final MemberService memberService;


    @Override
    public Boolean friendshipRequestExists(Long firstMemberId, Long SecondMemberId) {

        try {
            // Check if there is a friend request made from the first_member to the second_member
            Boolean friendshipExists = friendRep.friendshipRequestExists(firstMemberId, SecondMemberId);
            return friendshipExists;

        } catch (Exception e) {
            log.error("Error checking if friendship request exists: {}", e.getMessage());
            return false;
        }
        
    }

    @Override
    public Boolean friendshipRequestIsAccepted(String firstMemberEmail, String secondMemberEmail) {

        // Get the member information for both users
        Member firstMemberInfo = memberService.findMemberByEmail(firstMemberEmail);
        Member secondMemberInfo = memberService.findMemberByEmail(secondMemberEmail);

        Boolean friendshipRequestExists = this.friendshipRequestExists(firstMemberInfo.getId(), secondMemberInfo.getId());

        if (!friendshipRequestExists) {
            return false;
        }

        try {

            Boolean friendshipRequestIsAccepted = friendRep.friendshipRequestIsAccepted(firstMemberInfo.getId(), secondMemberInfo.getId());
            return friendshipRequestIsAccepted;

        } catch (Exception e) {
            log.error("Error checking if friendship request between member {} and member {} is accepted.\nError message: {}", firstMemberEmail, secondMemberEmail, e.getMessage());
            return false;
        }
    }

    @Override
    public String sendFriendshipRequest(String firstMemberEmail, String secondMemberEmail){
        String ret;

        if (firstMemberEmail.equals(secondMemberEmail)){
            ret = "You cannot send a friend request to yourself";
        } else {

            // Get the member information for both users
            Member firstMember = memberService.findMemberByEmail(firstMemberEmail);
            Member secondMember = memberService.findMemberByEmail(secondMemberEmail);

            Boolean friendshipRequestExists = this.friendshipRequestExists(firstMember.getId(), secondMember.getId());
            
            if (friendshipRequestExists) {
                ret = "Friend request failed, request is waiting to be accepted ";
            } else {

                try {

                    // Sets new friend request with the first member as the sender and the second member as the receiver
                    friendRep.save(new Friend(null, new Date(), null, firstMember, true, secondMember, false));

                    ret = "Friend request sent";

                } catch (Exception e) {
                    log.error("Error sending friend request from {} to {}: {}", firstMemberEmail, secondMemberEmail, e.getMessage());
                    ret = "Friend request failed, please try again later";
                }
                
            }
        }
        return ret;
    }

    @Override
    public String acceptFriendshipRequest(String firstMemberEmail, String secondMemberEmail) {
        String ret;

        if (firstMemberEmail.equals(secondMemberEmail)) {
            ret = "Trying to self accept friend request, error";
        } else {

            Member firstMember = memberService.findMemberByEmail(firstMemberEmail);
            Member secondMember = memberService.findMemberByEmail(secondMemberEmail);

            Boolean friendshipRequestExists = friendRep.friendshipRequestExists(firstMember.getId(), secondMember.getId());

            if (friendshipRequestExists) {

                Boolean isAccepted = friendRep.friendshipRequestIsAccepted(firstMember.getId(), secondMember.getId());

                if (!isAccepted) {

                    try {

                        friendRep.acceptFriendRequest(firstMember.getId(), secondMember.getId(), new Date());

                        ret = "Request accepted";

                    } catch (Exception e) {
                        log.error("Error accepting friend request from {} to {}: {}", firstMemberEmail, secondMemberEmail, e.getMessage());
                        ret = "REQ_ACCEPTANCE_ERROR";
                    }

                    // when friendship is made a chatroom for the new friends is made
                    chatService.saveChatroom(memberService.findMemberByEmail(firstMemberEmail), memberService.findMemberByEmail(secondMemberEmail));

                } else {
                    ret = "Already friends";
                }
            } else {
                ret = "Friend request no longer exists for some reason";
            }

        }
        
        return ret;
    }

    @Override
    public String declineFriendshipRequest(String firstMemberEmail, String secondMemberEmail) {
        String ret;
        if (firstMemberEmail.equals(secondMemberEmail)){
            ret = "Trying to self decline friend request, error";
        } else {

            // Get the member information for both users
            Member firstMember = memberService.findMemberByEmail(firstMemberEmail);
            Member secondMember = memberService.findMemberByEmail(secondMemberEmail);

            Boolean friendshipRequestExists = friendRep.friendshipRequestExists(firstMember.getId(), secondMember.getId());

            if (friendshipRequestExists) {
                
                Boolean isAccepted = friendRep.friendshipRequestIsAccepted(firstMember.getId(), secondMember.getId());

                if (!isAccepted) {

                    try {

                        friendRep.declineFriendRequest(firstMember.getId(), secondMember.getId());
                        
                        ret = "Request declined";
                        
                    } catch (Exception e) {
                        log.error("Error declining friend request from {} to {}: {}", firstMemberEmail, secondMemberEmail, e.getMessage());
                        ret = "REQ_DECLINE_ERROR";
                    }
                    
                } else {
                    ret = "Already friends";
                }
            } else {
                ret = "You have already declined this request, please refresh the page";
            }
        }
        return ret;
    }

    @Override
    public List<MemberInfo> getFriends(String memberEmail){

        Member member = memberService.findMemberByEmail(memberEmail);

        List<Object[]> friendsList = friendRep.findAllFriends(member.getId());

        List<MemberInfo> friendsInfo = new ArrayList<>();

        friendsList.forEach(friendInfo ->
            friendsInfo.add(
                new MemberInfo(
                    (Long) friendInfo[0],
                    friendInfo[2].toString(),
                    friendInfo[5].toString(),
                    friendInfo[1].toString(),
                    null
                )
            )
        );
        
        return friendsInfo;
    }

    @Override
    public List<String> getFriendshipRequests(String memberEmail){

        Member member = memberService.findMemberByEmail(memberEmail);
        return friendRep.findFriendRequests(member.getId());
    }
}
