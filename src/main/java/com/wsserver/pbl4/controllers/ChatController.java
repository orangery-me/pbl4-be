package com.wsserver.pbl4.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.wsserver.pbl4.models.ChatMessage;
import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.models.Notification;
import com.wsserver.pbl4.services.ChatMessageService;
import com.wsserver.pbl4.services.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template; // provides methods for sending messages to a user.
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    // /app/chat
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage message) {
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        System.out.println("Sending message to chat room: " + savedMessage.getChatRoomId());
        List<String> membersId = chatRoomService.getAllMembers(savedMessage.getChatRoomId());
        System.out.println("Members in the chat room: " + membersId);
        membersId.forEach(memberId -> {
            System.out.println("Sending message to " + memberId);
            // send notification to the queue /user/{uid}/queue/messages
            template.convertAndSendToUser(memberId, "/queue/messages",
                    ChatNotification.builder()
                            .chatRoomId(savedMessage.getChatRoomId())
                            .senderId(savedMessage.getSenderId())
                            .receiverId(memberId)
                            .notificationType(Notification.MESSAGE)
                            .isRead(false)
                            .content(savedMessage.getContent())
                            .timestamp(savedMessage.getTimestamp())
                            .build());
        });

    }

    @GetMapping("/getMessages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getMessages(chatRoomId));
    }
}
