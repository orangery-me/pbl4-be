package com.wsserver.pbl4.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatMessage {
    @Id
    private String id;
    private String chatRoomId;
    private String senderId;
    private String receiverId;
    private String content;
    private Date timestamp;
}
