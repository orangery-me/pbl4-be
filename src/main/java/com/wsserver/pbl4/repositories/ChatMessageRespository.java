package com.wsserver.pbl4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wsserver.pbl4.models.ChatMessage;

public interface ChatMessageRespository extends MongoRepository<ChatMessage, String> {
    Optional<List<ChatMessage>> findByChatRoomId(String chatRoomId);
}
