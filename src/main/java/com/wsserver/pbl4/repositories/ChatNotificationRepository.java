package com.wsserver.pbl4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.wsserver.pbl4.models.ChatNotification;

public interface ChatNotificationRepository extends MongoRepository<ChatNotification, String> {
    @Query(value = "{ 'receiver.uid': ?0, 'isRead': false }", sort = "{ 'timestamp': -1 }")
    Optional<List<ChatNotification>> findByReceiverUidAndIsReadFalse(String receiverUid);

    // @Query("{ 'chatRoomId': ?0, 'isRead': false }")
    // Optional<List<ChatNotification>>
    // findByChatRoomIdAndReceiverUidIsReadFalse(String roomId, String receiverId);

    @Aggregation(pipeline = {
            "{ $match: { 'receiver.uid': ?0, 'isRead': false } }", // Chỉ lấy thông báo cho userId nhất định và chưa đọc
            "{ $sort: { 'chatRoomId': 1, 'timestamp': -1 } }", // Sắp xếp theo chatRoomId và timestamp giảm dần
            "{ $group: { _id: '$chatRoomId', latestNotification: { $first: '$$ROOT' } } }", // Nhóm theo chatRoomId và
                                                                                            // lấy tin nhắn mới nhất
            "{ $replaceRoot: { newRoot: '$latestNotification' } }" // Thay thế root bằng tin nhắn mới nhất trong từng
                                                                   // phòng
    })
    Optional<List<ChatNotification>> findLatestNotificationsByChatRoomIdAndUserId(String receiverUid);

    Optional<ChatNotification> findById(String notificationId);
}
