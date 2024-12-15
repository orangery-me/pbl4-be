package com.wsserver.pbl4.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SignalingHandler extends TextWebSocketHandler {

    private static final Map<String, String> sessionToRoom = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        System.out.println("Client connected: " + sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Đọc tin nhắn từ client (có thể là SDP, ICE candidate, v.v.)
        String payload = message.getPayload();
        System.out.println("Received message from " + session.getId() + ": " + payload);

        // Giải mã tin nhắn JSON
        Map<String, Object> data = new ObjectMapper().readValue(payload, Map.class);
        String type = (String) data.get("type");
        System.out.println("Message type: " + type);

        switch (type) {
            case "join": // Client yêu cầu tham gia phòng
                handleJoin(session, data);
                break;
            case "offer":
            case "answer":
            case "candidate":
                handleSignaling(session, data);
                break;
            default:
                System.out.println("Unknown message type: " + type);
        }
    }

    private void handleJoin(WebSocketSession session, Map<String, Object> data) {
        try {
            String roomId = (String) data.get("roomId");
            // Lấy danh sách thành viên trong phòng (không bao gồm client mới)
            if (rooms.get(roomId) != null && rooms.get(roomId).size() > 1) {
                Map<String, Object> response = new ConcurrentHashMap<>();
                Set<String> members = new HashSet<>(rooms.get(roomId).keySet());
                members.remove(session.getId());

                response.put("type", "members");
                response.put("members", members);
                System.out.println("Members in room " + roomId + ": " + response.get("members"));
                // gửi danh sách thành viên cho client mới
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));
            }

            // Thêm client vào phòng
            rooms.putIfAbsent(roomId, new ConcurrentHashMap<>());
            rooms.get(roomId).put(session.getId(), session);
            sessionToRoom.put(session.getId(), roomId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void handleSignaling(WebSocketSession sender, Map<String, Object> data) {
        try {
            String roomId = sessionToRoom.get(sender.getId());
            if (roomId == null) {
                System.out.println("Sender " + sender.getId() + " is not in any room");
                return;
            }

            String senderId = sender.getId();
            String receiverId = (String) data.get("target");

            // Gửi tin nhắn cho người nhận
            WebSocketSession receiver = rooms.get(roomId).get(receiverId);
            // Thêm thông tin người gửi vào tin nhắn
            data.put("sender", senderId);
            if (receiver != null) {
                receiver.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
                System.out.println("Message sent from " + senderId + " to " + receiverId + ": " + data);
            } else {
                System.out.println("Receiver " + receiverId + " not found in room " + roomId);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        String roomId = sessionToRoom.get(sessionId);

        if (roomId != null) {
            Map<String, WebSocketSession> room = rooms.get(roomId);
            room.remove(sessionId);

            // Xóa phòng nếu rỗng
            if (room.isEmpty()) {
                rooms.remove(roomId);
            }

            sessionToRoom.remove(sessionId);
            // Gửi thông báo rời tới các thành viên còn lại
            Map<String, Object> response = new ConcurrentHashMap<>();
            response.put("type", "leave");
            response.put("member", sessionId);
            room.values().forEach(member -> {
                try {
                    member.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            });

            System.out.println("Client " + sessionId + " disconnected from room " + roomId);
        }
    }
}
