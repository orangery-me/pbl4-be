package com.wsserver.pbl4.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateChatRoomRequest {
    private String roomName;
    private String roomOwnerId;
}
