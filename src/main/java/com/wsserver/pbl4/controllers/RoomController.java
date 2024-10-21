package com.wsserver.pbl4.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wsserver.pbl4.DTOs.AddNewMemberRequest;
import com.wsserver.pbl4.DTOs.CreateChatRoomRequest;
import com.wsserver.pbl4.models.ChatRoom;
import com.wsserver.pbl4.models.PrivateChatRoom;
import com.wsserver.pbl4.services.ChatRoomService;
import com.wsserver.pbl4.services.PrivateChatRoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RoomController {
    final ChatRoomService chatRoomService;
    final PrivateChatRoomService pChatRoomService;

    @PostMapping("/createChatRoom")
    public ResponseEntity<String> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(request.getRoomName(), request.getRoomOwnerId(),
                request.getOtherMembersId()));
    }

    @PostMapping("/addNewMember")
    public ResponseEntity<String> addNewMember(@RequestBody AddNewMemberRequest request) {
        return ResponseEntity.ok(chatRoomService.addNewMember(request.getRoomId(), request.getNewMemberId()));
    }

    @GetMapping("/getJoinedRooms")
    public ResponseEntity<List<ChatRoom>> getJoinedRooms(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(chatRoomService.getJoinedRooms(userId));
    }

    @GetMapping("/getJoinedPrivateRooms")
    public ResponseEntity<List<PrivateChatRoom>> getJoinedPrivateRooms(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(pChatRoomService.getJoinedRooms(userId));
    }
}
