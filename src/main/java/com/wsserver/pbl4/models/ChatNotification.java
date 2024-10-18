package com.wsserver.pbl4.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ChatNotification {
    @Id
    private String notificationId;
    private String chatRoomId;
    private String senderId;
    private String receiverId;
    private String content;
    private Date timestamp;
    private Notification notificationType; // e.g., MESSAGE, ROOM_CREATED, etc.
    private boolean isRead;
}