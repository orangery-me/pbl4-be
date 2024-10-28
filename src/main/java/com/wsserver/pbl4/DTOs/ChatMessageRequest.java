package com.wsserver.pbl4.DTOs;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageRequest {
    private String chatRoomId;
    private String senderId;
    private String content;
    private MultipartFile file;
}