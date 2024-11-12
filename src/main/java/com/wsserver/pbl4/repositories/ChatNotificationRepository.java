package com.wsserver.pbl4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.wsserver.pbl4.models.ChatNotification;

public interface ChatNotificationRepository extends MongoRepository<ChatNotification, String> {
    @Query("{ 'receiver.uid': ?0, 'isRead': false }")
    Optional<List<ChatNotification>> findByReceiverUidAndIsReadFalse(String receiverUid);

    // @Query("{ 'chatRoomId': ?0, 'isRead': false }")
    // Optional<List<ChatNotification>>
    // findByChatRoomIdAndReceiverUidIsReadFalse(String roomId, String receiverId);

    Optional<ChatNotification> findById(String notificationId);
}
