package com.wsserver.pbl4.controllers;

import java.util.List;

import com.wsserver.pbl4.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wsserver.pbl4.DTOs.PrivateChatMessageRequest;
import com.wsserver.pbl4.DTOs.ChatMessageRequest;
import com.wsserver.pbl4.models.ChatMessage;
import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.models.Notification;
import com.wsserver.pbl4.models.PrivateChatMessage;
import com.wsserver.pbl4.services.ChatMessageService;
import com.wsserver.pbl4.services.ChatNotificationService;
import com.wsserver.pbl4.services.ChatRoomService;
import com.wsserver.pbl4.services.CloudinaryService;
import com.wsserver.pbl4.services.PrivateChatMessageService;
import com.wsserver.pbl4.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
//@RestController
public class ChatController {
    private final SimpMessagingTemplate template; // provides methods for sending messages to a user.
    private final ChatMessageService chatMessageService;
    private final PrivateChatMessageService privateChatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatNotificationService chatNotificationService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @PostMapping("/URLimage")
    public String URLimage(@RequestPart(value = "file", required = false) MultipartFile file) {

        String url = cloudinaryService.upload(file);

        return url;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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
        if (file != null)
            message.setFile(file);
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        processMessage(savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    public void processMessage(@Payload ChatMessage message) {
        List<String> membersId = chatRoomService.getAllMembers(message.getChatRoomId());

        membersId.forEach(memberId -> {
            ChatNotification noti = ChatNotification.builder()
                    .chatRoomId(message.getChatRoomId())
                    .sender(message.getSender())
                    .receiver(userService.findById(memberId))
                    .notificationType(Notification.MESSAGE)
                    .isRead(false)
                    .timestamp(message.getTimestamp())
                    .build();

            if (memberId.equals(message.getSender().getUid())) {
                noti.setRead(true);
            }

            // Gửi thông báo cho các thành viên trong phòng
            template.convertAndSendToUser(memberId, "/queue/messages", noti);
            chatNotificationService.createNotification(noti);
        });
    }
    public void processMessageNoti(@Payload ChatMessage message) {
        List<String> membersId = chatRoomService.getAllMembers(message.getChatRoomId());

        membersId.forEach(memberId -> {
            ChatNotification noti = ChatNotification.builder()
                    .chatRoomId(message.getChatRoomId())
                    .sender(message.getSender())
                    .receiver(userService.findById(memberId))
                    .notificationType(Notification.MEMBER_REMOVED)
                    .isRead(false)
                    .timestamp(message.getTimestamp())
                    .build();

            if (memberId.equals(message.getSender().getUid())) {
                noti.setRead(true);
            }

            // Gửi thông báo cho các thành viên trong phòng
            template.convertAndSendToUser(memberId, "/queue/messages", noti);
            chatNotificationService.createNotification(noti);
        });
    }
    public void processMessageNotiAddRoom(@Payload ChatMessage message) {
        List<String> membersId = chatRoomService.getAllMembers(message.getChatRoomId());

        membersId.forEach(memberId -> {
            ChatNotification noti = ChatNotification.builder()
                    .chatRoomId(message.getChatRoomId())
                    .sender(message.getSender()) // người được thêm vô nhóm
                    .receiver(userService.findById(memberId)) // người nhận noti
                    .notificationType(Notification.MEMBER_ADDED)
                    .isRead(false)
                    .timestamp(message.getTimestamp())
                    .build();

//            if (memberId.equals(message.getSender().getUid())) {
//                noti.setRead(true);
//            }

            // Gửi thông báo cho các thành viên trong phòng
            template.convertAndSendToUser(memberId, "/queue/messages", noti);
            chatNotificationService.createNotification(noti);
        });
    }
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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
        ChatNotification receiverNoti = ChatNotification.builder()
                .chatRoomId(message.getChatRoomId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .notificationType(Notification.MESSAGE)
                .isRead(false)
                .timestamp(message.getTimestamp())
                .build();

        ChatNotification senderNoti = ChatNotification.builder()
                .chatRoomId(message.getChatRoomId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .notificationType(Notification.MESSAGE)
                .isRead(true)
                .timestamp(message.getTimestamp())
                .build();

        chatNotificationService.createNotification(receiverNoti);
        chatNotificationService.createNotification(senderNoti);

        // send notification to the queue /user/{uid}/queue/messages
        template.convertAndSendToUser(message.getReceiver().getUid(), "/queue/messages",
                receiverNoti);

        template.convertAndSendToUser(message.getSender().getUid(), "/queue/messages",
                senderNoti);

    }
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @GetMapping("/getMessages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getMessages(chatRoomId));
    }
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @GetMapping("/getPrivateMessages/{chatRoomId}")
    public ResponseEntity<List<PrivateChatMessage>> getPrivateMessages(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(privateChatMessageService.getMessages(chatRoomId));
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @PostMapping("/leaveChatRoom/{roomId}")
    public ResponseEntity<String> leaveChatRoom(
    @PathVariable("roomId") String roomId,
    @RequestParam("userId") String userId) {
    User user = userService.findById(userId);
    String mes = user.getFullname() + " da roi nhom";
    try {
        ChatMessageRequest chatMessage = new ChatMessageRequest();
        chatMessage.setChatRoomId(roomId);
        chatMessage.setSenderId(user.getUid());
        chatMessage.setContent(mes);
        ChatMessage chatMessage1 = chatMessageService.saveMessage(chatMessage);
        processMessageNoti(chatMessage1);

        String message = chatRoomService.leaveChatRoom(roomId, userId);
        return ResponseEntity.ok(message);

    } catch (RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @PostMapping("/addMemberChatRoom/{roomId}")
    public ResponseEntity<String> addMemberChatRoom(
            @PathVariable("roomId") String roomId,
            @RequestParam("userId") String userId) {
        User user = userService.findById(userId);
        String mes = user.getFullname() + " da them vao nhom";
        try {
            ChatMessageRequest chatMessage = new ChatMessageRequest();
            chatMessage.setChatRoomId(roomId);
            chatMessage.setSenderId(user.getUid());
            chatMessage.setContent(mes);
            ChatMessage chatMessage1 = chatMessageService.saveMessage(chatMessage);
            processMessageNotiAddRoom(chatMessage1);

            String message = chatRoomService.addNewMember(roomId, userId);
            return ResponseEntity.ok(message);

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}