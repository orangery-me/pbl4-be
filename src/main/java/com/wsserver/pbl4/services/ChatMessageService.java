package com.wsserver.pbl4.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wsserver.pbl4.models.ChatMessage;
import com.wsserver.pbl4.repositories.ChatMessageRespository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRespository repository;
    // private final ChatRoomService chatRoomService;

    public ChatMessage saveMessage(ChatMessage message) {
        repository.save(message);
        return message;
    }

    public List<ChatMessage> getMessages(String chatRoomId) {
        // String chatRoomId = chatRoomService.getChatRoomId(senderId, receiverId,
        // false).orElse(null);
        // .orElseThrow(
        // () -> new RuntimeException("Chat room not found"));
        return repository.findByChatRoomId(chatRoomId).orElse(null);
    }
}
