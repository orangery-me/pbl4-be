package com.wsserver.pbl4.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.wsserver.pbl4.models.ChatNotification;
import com.wsserver.pbl4.services.ChatNotificationService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class NotificationController {
    private final ChatNotificationService chatNotificationService;

    @GetMapping("/getNotifications/{userId}")
    public ResponseEntity<List<ChatNotification>> getNotifications(@PathVariable("userId") String userId) {
        return chatNotificationService.getNotificationsByUser(userId)
                .map(result -> ResponseEntity.ok(result))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ArrayList<>()));
    }

    @GetMapping("/markAsRead/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable("notificationId") String notificationId) {
        chatNotificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Marked as read");
    }

    @GetMapping("/getNotificationsByRoom")
    public ResponseEntity<List<ChatNotification>> getNotificationsByRoom(@RequestParam("chatRoomId") String chatRoomId,
            @RequestParam("receiverId") String receiverId) {
        return chatNotificationService.getNotificationsByRoom(chatRoomId, receiverId)
                .map(result -> ResponseEntity.ok(result))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ArrayList<>()));
    }

}
