package com.wsserver.pbl4.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.wsserver.pbl4.models.ChatRoom;
import com.wsserver.pbl4.models.PrivateChatRoom;
import com.wsserver.pbl4.repositories.PrivateChatRoomRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PrivateChatRoomService {
    private final PrivateChatRoomRepository repository;

    public String createPrivateChatRoom(String user1Id, String user2Id) {
        List<String> sortedIds = Arrays.asList(user1Id, user2Id);
        Collections.sort(sortedIds);
        String roomName = sortedIds.get(0) + "_" + sortedIds.get(1);
        PrivateChatRoom chatRoom = PrivateChatRoom.builder()
                .id("pr" + roomName)
                .roomName(roomName)
                .user1Id(sortedIds.get(0))
                .user2Id(sortedIds.get(1))
                .build();
        repository.save(chatRoom);
        return chatRoom.getId();
    }

    public String getPrivateChatRoomName(String user1Id, String user2Id) {
        List<String> sortedIds = Arrays.asList(user1Id, user2Id);
        Collections.sort(sortedIds);
        return sortedIds.get(0) + "_" + sortedIds.get(1);
    }

    public Optional<String> getPrivateChatRoomId(String user1Id, String user2Id) {
        String roomName = getPrivateChatRoomName(user1Id, user2Id);
        return repository.findByRoomName(roomName).map(PrivateChatRoom::getId);
    }

    public List<PrivateChatRoom> getJoinedRooms(String userId) {
        List<PrivateChatRoom> chatRooms = repository.findAll();
        List<PrivateChatRoom> joinedRooms = new ArrayList<>();

        for (PrivateChatRoom chatRoom : chatRooms) {
            if (chatRoom.getUser1Id().equals(userId) || chatRoom.getUser2Id().equals(userId)) {
                joinedRooms.add(chatRoom);
            }
        }
        return joinedRooms;
    }

    public PrivateChatRoom findById(String id) {
        try{
            PrivateChatRoom chatRoom = repository.findById(id).orElse(null);
            return chatRoom;
        } catch (Exception e) {
            System.out.println("llllll" +e.getLocalizedMessage());
        }
        return null;
    }
}
