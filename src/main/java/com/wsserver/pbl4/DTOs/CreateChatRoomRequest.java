package com.wsserver.pbl4.DTOs;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateChatRoomRequest {
    private String roomName;
    private MultipartFile avatar;
    private String roomOwnerId;
    private List<String> otherMembersId;
}
