package com.wsserver.pbl4.DTOs;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.wsserver.pbl4.models.Notification;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ChatNotificationRequest {
    @Id
    private String notificationId;
    private String chatRoomId;
    private String senderId;
    private String receiverId;
    private Date timestamp;
    private String message;
    private Notification notificationType; // e.g., MESSAGE, ROOM_CREATED, etc.
    private boolean isRead;
}
