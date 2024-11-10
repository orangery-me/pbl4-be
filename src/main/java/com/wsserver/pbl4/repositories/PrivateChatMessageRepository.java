package com.wsserver.pbl4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wsserver.pbl4.models.PrivateChatMessage;

public interface PrivateChatMessageRepository extends MongoRepository<PrivateChatMessage, String> {
    Optional<List<PrivateChatMessage>> findByChatRoomId(String chatRoomId);

}
