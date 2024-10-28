package com.wsserver.pbl4.DTOs;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrivateChatMessageRequest {
    private String senderId;
    private String receiverId;
    private String content;
    private MultipartFile file;
}
