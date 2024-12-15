package com.wsserver.pbl4.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wsserver.pbl4.DTOs.AddNewMemberRequest;
import com.wsserver.pbl4.DTOs.CreateChatRoomRequest;
import com.wsserver.pbl4.models.ChatRoom;
import com.wsserver.pbl4.models.PrivateChatRoom;
import com.wsserver.pbl4.services.ChatRoomService;
import com.wsserver.pbl4.services.PrivateChatRoomService;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
public class RoomController {
    final ChatRoomService chatRoomService;
    final PrivateChatRoomService pChatRoomService;

    @CrossOrigin(origins = "http://172.20.1.74:8080")
    @PostMapping("/createChatRoom")
    public ResponseEntity<ChatRoom> createChatRoom(
            @RequestParam("roomName") String roomName,
            @RequestParam("roomOwnerId") String roomOwnerId,
            @RequestParam("otherMembersId") List<String> otherMembersId,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        CreateChatRoomRequest request = CreateChatRoomRequest.builder()
                .roomName(roomName)
                .avatar(avatar)
                .roomOwnerId(roomOwnerId)
                .otherMembersId(otherMembersId)
                .build();
        return ResponseEntity
                .ok(chatRoomService.createChatRoom(request));
    }

    @CrossOrigin(origins = "http://172.20.1.74:8080")
    @PostMapping("/addNewMember")
    public ResponseEntity<String> addNewMember(@RequestBody AddNewMemberRequest request) {
        return ResponseEntity.ok(chatRoomService.addNewMember(request.getRoomId(), request.getNewMemberId()));
    }

    @CrossOrigin(origins = "http://172.20.1.74:8080")
    @GetMapping("/getJoinedRooms")
    public ResponseEntity<List<ChatRoom>> getJoinedRooms(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(chatRoomService.getJoinedRooms(userId));
    }

    @CrossOrigin(origins = "http://172.20.1.74:8080")
    @GetMapping("/getJoinedPrivateRooms")
    public ResponseEntity<List<PrivateChatRoom>> getJoinedPrivateRooms(@RequestParam("userId") String userId) {
        try {
            List<PrivateChatRoom> rooms = pChatRoomService.getJoinedRooms(userId);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    @CrossOrigin(origins = "http://172.20.1.74:8080")
    @GetMapping("/findRoomById")
    public ResponseEntity<ChatRoom> getRoomById(@RequestParam("Id") String Id) {
        return ResponseEntity.ok(chatRoomService.findById(Id));
    }

    @CrossOrigin(origins = "http://172.20.1.74:8080")
    @GetMapping("/findPrivateRoomById")
    public ResponseEntity<PrivateChatRoom> getPrivateRoomById(@RequestParam("Id") String Id) {
        return ResponseEntity.ok(pChatRoomService.findById(Id));
    }

}
