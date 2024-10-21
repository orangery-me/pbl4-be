package com.wsserver.pbl4.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wsserver.pbl4.models.PrivateChatMessage;

public interface PrivateChatMessageRepository extends MongoRepository<PrivateChatMessage, String> {
}
