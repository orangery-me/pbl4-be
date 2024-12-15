package com.wsserver.pbl4.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.repositories.ChatNotificationRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ChatNotificationService {
    private final ChatNotificationRepository repository;

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

    public ChatNotification createNotification(ChatNotification notification) {
        repository.save(notification);
        return notification;
    }

    public void markAsRead(String notificationId) {
        ChatNotification noti = repository.findById(notificationId).orElse(null);
        if (noti != null) {
            noti.setRead(true);
            repository.save(noti);
        }
    }

    public Optional<List<ChatNotification>> getLatestNotificationsByChatRoomIdAndUserId(String receiverId) {
        return repository.findLatestNotificationsByChatRoomIdAndUserId(receiverId);
    }

}
