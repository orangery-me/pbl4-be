package com.wsserver.pbl4.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wsserver.pbl4.models.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
