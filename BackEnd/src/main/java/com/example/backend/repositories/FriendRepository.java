package com.example.backend.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.models.Friend;


public interface FriendRepository extends JpaRepository<Friend, Long> {

    // checks if a "friendship" between the two members exists
    @Query(
        " SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
        " FROM Friend f " +
        " WHERE (f.firstMember.id = :firstMemberId AND f.secondMember.id = :secondMemberId) " +
        " OR (f.firstMember.id = :secondMemberId AND f.secondMember.id = :firstMemberId) ")
    Boolean friendshipRequestExists(@Param("firstMemberId") Long firstMemberId, @Param("secondMemberId") Long secondMemberId);

    // checks if the "friendship" is accepted by both members
    @Query(
        " SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
        " FROM Friend f " +
        " WHERE ((f.firstMember.id = :firstMemberId AND f.secondMember.id = :secondMemberId) " +
        " OR (f.firstMember.id = :secondMemberId AND f.secondMember.id = :firstMemberId)) " +
        " AND f.firstMemberAccepted = true " +
        " AND f.secondMemberAccepted = true")
    Boolean friendshipRequestIsAccepted(@Param("firstMemberId") Long firstMemberId, @Param("secondMemberId") Long secondMemberId);

    // accepts the request made from first_member to second_member
    @Modifying
    @Query("Update Friend f SET f.secondMemberAccepted = TRUE, f.requestAcceptanceDate = :requestAcceptanceDate " +
            "WHERE f.firstMember.id = :firstMemberId AND f.secondMember.id = :secondMemberId"
            )
    void acceptFriendRequest(@Param("firstMemberId") Long firstMemberId, @Param("secondMemberId") Long secondMemberId, @Param("requestAcceptanceDate") Date requestAcceptanceDate);

    @Modifying
    @Query(value = "DELETE FROM friend " +
            "WHERE first_member_id = :first AND second_member_id = :second " +
            "AND second_member_accepted = FALSE",
            nativeQuery = true)
    void declineFriendRequest(Long first, Long second);

    // returns list of the friends(MemberInfo) of the member_email's put as 'first_member_email'
    @Query(value =
        "SELECT m.*" +
        "FROM member m " +
        "WHERE m.id IN " +
        "(Select second_member_id " +
        " FROM friend " +
        " WHERE first_member_id = :member_id AND second_member_accepted = TRUE)",
        nativeQuery = true)
    List<Object[]> findFriendsAsFirstMember(Long member_id);

    // returns list of the friends(MemberInfo) of the member_email's put as 'second_member_email'
    @Query(value =
        "SELECT m.* " +
        "FROM member m " +
        "WHERE m.id IN " +
        "(Select first_member_id " +
        " FROM friend " +
        " WHERE second_member_id = :member_id AND second_member_accepted = TRUE)",
        nativeQuery = true)
    List<Object[]> findFriendsAsSecondMember(Long member_id);

    @Query(value = 
        "SELECT m.* " +
        "FROM member m " +
        "WHERE m.id IN (" +
        "Select second_member_id " +
        "   FROM friend " +
        "   WHERE first_member_id = :member_id AND second_member_accepted = TRUE " +
        "   UNION " +
        "   Select first_member_id " +
        "   FROM friend " +
        "   WHERE second_member_id = :member_id AND second_member_accepted = TRUE " +
        ")",
        nativeQuery = true)
    List<Object[]> findAllFriends(Long member_id);
    
    // returns the members that have friend requested the member with 'member_email'
    @Query(value = "SELECT m.email " +
            "FROM member m " +
            "WHERE m.id IN " +
            "(SELECT first_member_id " +
            "FROM friend f " +
            "WHERE second_member_id = :member_id AND second_member_accepted = FALSE)",
            nativeQuery = true)
    List<String> findFriendRequests(Long member_id);
}
