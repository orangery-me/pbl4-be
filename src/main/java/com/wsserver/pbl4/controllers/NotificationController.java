package com.wsserver.pbl4.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.services.ChatNotificationService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
public class NotificationController {
    private final ChatNotificationService chatNotificationService;

    @GetMapping("/getNotifications/{userId}")
    public ResponseEntity<List<ChatNotification>> getNotifications(@PathVariable("userId") String userId) {
        List<ChatNotification> noti = chatNotificationService.getNotificationsByUser(userId).orElse(new ArrayList<>());
        System.out.println("noti nek: "+noti);
        return ResponseEntity.ok(noti);
                //.map(result -> ResponseEntity.ok(result));
//                .orElseGet(() -> ResponseEntity.badRequest().body(new ArrayList<>()));
    }

    @GetMapping("/markAsRead/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable("notificationId") String notificationId) {
        chatNotificationService.markAsRead(notificationId);
        System.out.println("Marked as read");
        return ResponseEntity.ok("Marked as read");
    }

    @GetMapping("/getNotificationsByRoom")
    public ResponseEntity<List<ChatNotification>> getNotificationsByRoom(@RequestParam("chatRoomId") String chatRoomId,
            @RequestParam("receiverId") String receiverId) {
        return chatNotificationService.getNotificationsByRoom(chatRoomId, receiverId)
                .map(result -> ResponseEntity.ok(result))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ArrayList<>()));
    }

    @GetMapping("/getLatestNotificationsByChatRoomIdAndUserId/{receiverId}")
    public ResponseEntity<List<ChatNotification>> getLatestNotificationsByChatRoomIdAndUserId(
            @PathVariable("receiverId") String receiverId) {
        return chatNotificationService.getLatestNotificationsByChatRoomIdAndUserId(receiverId)
                .map(result -> ResponseEntity.ok(result))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ArrayList<>()));
    }

}
