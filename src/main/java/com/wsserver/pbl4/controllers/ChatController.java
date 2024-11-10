package com.wsserver.pbl4.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.wsserver.pbl4.DTOs.PrivateChatMessageRequest;
import com.wsserver.pbl4.DTOs.ChatMessageRequest;
import com.wsserver.pbl4.models.ChatMessage;
import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.models.Notification;
import com.wsserver.pbl4.models.PrivateChatMessage;
import com.wsserver.pbl4.services.ChatMessageService;
import com.wsserver.pbl4.services.ChatRoomService;
import com.wsserver.pbl4.services.PrivateChatMessageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template; // provides methods for sending messages to a user.
    private final ChatMessageService chatMessageService;
    private final PrivateChatMessageService privateChatMessageService;
    private final ChatRoomService chatRoomService;
    // private final UserService userService;

    @PostMapping("/sendMessageToRoom")
    public ResponseEntity<ChatMessage> sendMessageToRoom(
            @RequestParam("chatRoomId") String chatRoomId,
            @RequestParam("senderId") String senderId,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ChatMessageRequest message = new ChatMessageRequest();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setFile(file);
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        processMessage(savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    public void processMessage(@Payload ChatMessage message) {
        List<String> membersId = chatRoomService.getAllMembers(message.getChatRoomId());

        membersId.forEach(memberId -> {
            System.out.println("Sending message to " + memberId);

            // Gửi thông báo cho các thành viên trong phòng
            template.convertAndSendToUser(memberId, "/queue/messages",
                    ChatNotification.builder()
                            .chatRoomId(message.getChatRoomId())
                            .senderId(message.getSender().getUid())
                            .receiverId(memberId)
                            .notificationType(Notification.MESSAGE)
                            .isRead(false)
                            .content(message.getContent()) // Nội dung hoặc URL ảnh
                            .timestamp(message.getTimestamp())
                            .build());
        });
    }

    @PostMapping("/sendMessageToUser")
    public ResponseEntity<PrivateChatMessage> sendMessageToUser(
            @RequestParam("senderId") String senderId,
            @RequestParam("receiverId") String receiverId,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        PrivateChatMessageRequest message = new PrivateChatMessageRequest();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        if (file != null)
            message.setFile(file);
        PrivateChatMessage savedMessage = privateChatMessageService.saveMessage(message);
        processPrivateMessage(savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    public void processPrivateMessage(@Payload PrivateChatMessage message) {
        ChatNotification noti  = ChatNotification.builder()
        .chatRoomId(message.getChatRoomId())
        .senderId(message.getSender().getUid())
        .receiverId(message.getReceiver().getUid())
        .notificationType(Notification.MESSAGE)
        .isRead(false)
        .content(message.getContent())
        .timestamp(message.getTimestamp())
        .build();

        System.out.println("Noti test: " + noti);

        // send notification to the queue /user/{uid}/queue/messages
        template.convertAndSendToUser(message.getReceiver().getUid(), "/queue/messages",
                noti);
    }

    @GetMapping("/getMessages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getMessages(chatRoomId));
    }

    @GetMapping("/getPrivateMessages/{chatRoomId}")
    public ResponseEntity<List<PrivateChatMessage>> getPrivateMessages(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(privateChatMessageService.getMessages(chatRoomId));
    }
    
}