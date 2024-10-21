package com.wsserver.pbl4.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.wsserver.pbl4.DTOs.PrivateChatMessageDTO;
import com.wsserver.pbl4.models.PrivateChatMessage;
import com.wsserver.pbl4.repositories.PrivateChatMessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrivateChatMessageService {
    private PrivateChatMessageRepository repository;
    private PrivateChatRoomService roomService;

    public PrivateChatMessage saveMessage(PrivateChatMessageDTO message) {
        String chatRoomId = roomService.getPrivateChatRoomId(message.getSenderId(), message.getReceiverId())
                .orElseGet(() -> {
                    return roomService.createPrivateChatRoom(message.getSenderId(), message.getReceiverId());
                });
        PrivateChatMessage savedMessage = PrivateChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .timestamp(new Date())
                .build();
        repository.save(savedMessage);
        return savedMessage;
    }
}
