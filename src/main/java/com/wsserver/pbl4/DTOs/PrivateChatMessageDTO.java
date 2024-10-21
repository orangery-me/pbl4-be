package com.wsserver.pbl4.DTOs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrivateChatMessageDTO {
    private String senderId;
    private String receiverId;
    private String content;
}
