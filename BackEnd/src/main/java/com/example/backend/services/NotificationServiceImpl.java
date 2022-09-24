package com.example.backend.services;

import com.example.backend.models.Member;
import com.example.backend.models.NotificationDTO;
import com.example.backend.models.Notifications;
import com.example.backend.repositories.MemberRepository;
import com.example.backend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notifyRep;
    private final MemberRepository memberRep;

    @Override
    public List<NotificationDTO> getMemberNotifications(String email){

        Member member = memberRep.findByEmail(email);

        List<Notifications> list = notifyRep.findNotifications(member.getId());
        List<NotificationDTO> notifications = new ArrayList<>();

        list.forEach(notification -> notifications.add(
                new NotificationDTO(notification.getNotification_id(), notification.getMessage(), notification.getDateCreated())));
        return notifications;
    }

    @Override
    public String seenNotification(Long id){
        notifyRep.seeNotification(id);
        return "Notification seen";
    }

    @Override
    public void saveNotification(String text, String email){
        Member member = memberRep.findByEmail(email);
        notifyRep.save(new Notifications(null, text, new Date(), false, member));
    }
}
