package com.example.backend.repositories;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.backend.models.Friend;


public interface FriendRepository extends JpaRepository<Friend, Long> {

    // checks if a "friendship" between the two members exists
    @Query(value = "SELECT CASE WHEN (f.first_member_accepted = TRUE AND COUNT(*) > 0) " +
                    "THEN TRUE " +
                    "ELSE FALSE " +
                    "END " +
                    "FROM friend f " +
                    "WHERE f.first_member_id = :first AND f.second_member_id = :second",
            nativeQuery = true)
    BigInteger friendshipRequestExists(Long first, Long second);

    // checks if the "friendship" is accepted by both members
    @Query(value = "SELECT CASE WHEN (f.first_member_accepted = TRUE AND f.second_member_accepted = TRUE AND COUNT(*) > 0) " +
                    "THEN TRUE " +
                    "ELSE FALSE " +
                    "END " +
                    "FROM friend f " +
                    "WHERE f.first_member_id = :first AND f.second_member_id = :second",
            nativeQuery = true)
    BigInteger friendshipRequestIsAccepted(Long first, Long second);

    // accepts the request made from first_member to second_member
    @Modifying
    @Query(value = "Update friend f SET f.second_member_accepted = TRUE " +
            "WHERE f.first_member_id = :first AND f.second_member_id = :second",
            nativeQuery = true)
    void acceptFriendRequest(Long first, Long second);

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
