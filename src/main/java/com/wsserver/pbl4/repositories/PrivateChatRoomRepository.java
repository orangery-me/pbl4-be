package com.wsserver.pbl4.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wsserver.pbl4.models.PrivateChatRoom;

public interface PrivateChatRoomRepository extends MongoRepository<PrivateChatRoom, String> {
    Optional<PrivateChatRoom> findByRoomName(String roomName);
}
