package com.example.backend.repositories;

import com.example.backend.models.Member;
import com.example.backend.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    @Query(value = "SELECT * " +
            "FROM notifications " +
            "WHERE referenced_member_id = :member_id AND is_read = FALSE",
            nativeQuery = true)
    List<Notifications> findNotifications(Long member_id);

    @Modifying
    @Query(value = "Update notifications n SET n.is_read = TRUE " +
            "WHERE n.notification_id = :id", nativeQuery = true)
    void seeNotification(Long id);
}
