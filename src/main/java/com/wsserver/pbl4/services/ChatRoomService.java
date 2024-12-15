package com.wsserver.pbl4.services;

import org.springframework.stereotype.Service;

import com.wsserver.pbl4.DTOs.CreateChatRoomRequest;
import com.wsserver.pbl4.models.ChatRoom;
import com.wsserver.pbl4.models.User;
import com.wsserver.pbl4.repositories.ChatRoomRepository;
import java.util.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository repository;
    private final CloudinaryService cloudinaryService;

    public ChatRoom createChatRoom(CreateChatRoomRequest request) {
        List<String> membersId = request.getOtherMembersId() == null ? new ArrayList<>() : request.getOtherMembersId();
        membersId.add(request.getRoomOwnerId());
        String avatar = cloudinaryService.upload(request.getAvatar());

        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(request.getRoomName())
                .avatar(avatar)
                .roomOwnerId(request.getRoomOwnerId())
                .membersId(membersId)
                .build();
        repository.save(chatRoom);
        return chatRoom;
    }

    public String addNewMember(String roomId, String newMemberId) {
        ChatRoom chatRoom = repository.findById(roomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
        List<String> membersId = chatRoom.getMembersId();
        membersId.add(newMemberId);
        chatRoom.setMembersId(membersId);
        repository.save(chatRoom);
        return newMemberId;
    }

    public List<ChatRoom> getJoinedRooms(String userId) {
        List<ChatRoom> chatRooms = repository.findAll();
        List<ChatRoom> joinedRooms = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getMembersId().contains(userId)) {
                joinedRooms.add(chatRoom);
            }
        }
        return joinedRooms;
    }

    public List<String> getAllMembers(String roomId) {
        ChatRoom chatRoom = repository.findById(roomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
        return chatRoom.getMembersId();
    }

    public ChatRoom findById(String id) {
        ChatRoom chatRoom = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return chatRoom;
    }
    
    public String leaveChatRoom(String roomId, String userId) {
    ChatRoom chatRoom = repository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Chat room not found"));

    List<String> membersId = chatRoom.getMembersId();

    if (!membersId.contains(userId)) {
        throw new RuntimeException("User is not a member of this chat room");
    }

    membersId.remove(userId);
    
    if (chatRoom.getRoomOwnerId().equals(userId)) {
        if (membersId.isEmpty()) {
            repository.delete(chatRoom);
            return "Chat room deleted as the owner left and no members remain";
        } else {
            chatRoom.setRoomOwnerId(membersId.get(0));
        }
    }

    chatRoom.setMembersId(membersId);
    repository.save(chatRoom);

    return "User has left the chat room";
}
}
