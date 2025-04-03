package com.example.backend.services;

import com.example.backend.models.Friend;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.repositories.FriendRepository;
import com.example.backend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRep;
    private final MemberRepository memberRep;
    private final ChatroomService chatService;
    private final MemberService memberService;


    @Override
    public String friendRequest(String sender_email, String acceptor_email){
        String ret;
        if (sender_email.equals(acceptor_email)){
            ret = "You cannot send a friend request to yourself";
        }
        else {

            // sender is the member making the request
            // acceptor is the member being requested
            Member sender = memberRep.findByEmail(sender_email);
            Member acceptor = memberRep.findByEmail(acceptor_email);

            // checks if members are already friends
            // if a friendship exists it cannot exist again with inverted members
            BigInteger exists = friendRep.friendshipRequestExists(sender.getId(), acceptor.getId());
            BigInteger exists_inverted = friendRep.friendshipRequestExists(acceptor.getId(), sender.getId());
            if (exists.equals(BigInteger.ONE) || exists_inverted.equals(BigInteger.ONE)) {
                ret = "Friend request failed, request is waiting to be accepted ";
            } else {
                // basically makes the request, sender has accepted the friendship, acceptor hasn't
                friendRep.save(new Friend(null, null, sender, true, acceptor, false));
                //log.info("{} has sent {} a friend request", sender_email, acceptor_email);
                ret = "Friend request sent";
            }
        }
        return ret;
    }

    @Override
    public String acceptFriendRequest(String sender_email, String acceptor_email) {
        String ret;
        if (sender_email.equals(acceptor_email)) {
            ret = "Trying to self accept friend request, error";
        } else {
            // sender is the member making the request
            // acceptor is the member being requested
            Member sender = memberRep.findByEmail(sender_email);
            Member acceptor = memberRep.findByEmail(acceptor_email);


            // check if there is a friend request made from the first_member to the second_member
            BigInteger exists = friendRep.friendshipRequestExists(sender.getId(), acceptor.getId());
            if (exists.equals(BigInteger.ONE)) {
                // request exists, check if it is accepted, else accept it
                BigInteger isAccepted = friendRep.friendshipRequestIsAccepted(sender.getId(), acceptor.getId());
                if (isAccepted.equals(BigInteger.ZERO)) {
                    friendRep.acceptFriendRequest(sender.getId(), acceptor.getId());
                    ret = "Request accepted";

                    // when friendship is made a chatroom for the new friends is made
                    chatService.saveChatroom(memberService.findMemberByEmail(sender_email), memberService.findMemberByEmail(acceptor_email));

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
    public String declineFriendRequest(String sender_email, String acceptor_email) {
        String ret;
        if (sender_email.equals(acceptor_email)){
            ret = "Trying to self decline friend request, error";
        } else {
            // sender is the member making the request
            // acceptor is the member being requested
            Member sender = memberRep.findByEmail(sender_email);
            Member acceptor = memberRep.findByEmail(acceptor_email);

            // check if there is a friend request made from the first_member to the second_member
            BigInteger exists = friendRep.friendshipRequestExists(sender.getId(), acceptor.getId());
            if (exists.equals(BigInteger.ONE)) {
                // request exists, check if it is accepted, else decline it
                BigInteger isAccepted = friendRep.friendshipRequestIsAccepted(sender.getId(), acceptor.getId());
                if (isAccepted.equals(BigInteger.ZERO)) {
                    friendRep.declineFriendRequest(sender.getId(), acceptor.getId());
                    ret = "Request declined";
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
    public List<MemberInfo> getFriends(String member_email){

        Member member = memberRep.findByEmail(member_email);

        List<Object[]> list1 = friendRep.findFriendsAsFirstMember(member.getId());
        list1.addAll(friendRep.findFriendsAsSecondMember(member.getId()));

        List<MemberInfo> friendsInfo = new ArrayList<>();
        list1.forEach(object ->
                friendsInfo.add(new MemberInfo(Long.parseLong(object[0].toString()) , object[2].toString(),
                        object[5].toString(), object[1].toString(), null)));
        return friendsInfo;
    }

    @Override
    public Boolean areFriends(String mail1, String mail2) {

        Member member1 = memberRep.findByEmail(mail1);
        Member member2 = memberRep.findByEmail(mail2);

        BigInteger friends = friendRep.friendshipRequestIsAccepted(member1.getId(), member2.getId());
        BigInteger friends_inverse = friendRep.friendshipRequestIsAccepted(member2.getId(), member1.getId());
        return friends.equals(BigInteger.ONE) || friends_inverse.equals(BigInteger.ONE);
    }

    @Override
    public List<String> getFriendsRequests(String member_email){

        Member member = memberRep.findByEmail(member_email);
        return friendRep.findFriendRequests(member.getId());
    }
}
