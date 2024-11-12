package com.wsserver.pbl4.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wsserver.pbl4.DTOs.ChatNotificationRequest;
import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.repositories.ChatNotificationRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ChatNotificationService {
    private final ChatNotificationRepository repository;
    private final UserService userService;

    public Optional<List<ChatNotification>> getNotificationsByUser(String receiverId) {
        return repository.findByReceiverUidAndIsReadFalse(receiverId);
    }

    public Optional<List<ChatNotification>> getNotificationsByRoom(String chatRoomId, String receiverId) {
        Optional<List<ChatNotification>> res = repository.findByReceiverUidAndIsReadFalse(receiverId);
        res = res.filter(notifications -> {
            for (ChatNotification noti : notifications) {
                if (noti.getChatRoomId().equals(chatRoomId)) {
                    return true;
                }
            }
            return false;
        });
        System.out.println("res" + res);
        return res;
    }

    public ChatNotification createNotification(ChatNotificationRequest notification) {
        ChatNotification noti = ChatNotification.builder()
                .chatRoomId(notification.getChatRoomId())
                .sender(userService.findById(notification.getSenderId()))
                .receiver(userService.findById(notification.getReceiverId()))
                .timestamp(notification.getTimestamp())
                .notificationType(notification.getNotificationType())
                .isRead(notification.isRead())
                .build();
        repository.save(noti);
        return noti;
    }

    public void markAsRead(String notificationId) {
        ChatNotification noti = repository.findById(notificationId).orElse(null);
        if (noti != null) {
            noti.setRead(true);
            repository.save(noti);
        }
    }

}
