package com.example.backend.services;

import com.example.backend.models.NotificationDTO;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getMemberNotifications(String email);

    String seenNotification(Long id);

    void saveNotification(String text, String email);
}
