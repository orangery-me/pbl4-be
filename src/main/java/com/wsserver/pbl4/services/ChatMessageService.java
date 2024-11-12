package com.wsserver.pbl4.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import com.wsserver.pbl4.DTOs.ChatMessageRequest;
import com.wsserver.pbl4.models.ChatMessage;
import com.wsserver.pbl4.models.User;
import com.wsserver.pbl4.repositories.ChatMessageRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;

    public ChatMessage saveMessage(ChatMessageRequest message) {
        User sender = null;
        try {
            sender = userService.findById(message.getSenderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String imageURl = "";
        if (message.getFile() != null)
            imageURl = cloudinaryService.upload(message.getFile());
        ChatMessage savedMessage = ChatMessage.builder()
                .chatRoomId(message.getChatRoomId())
                .sender(sender)
                .content(message.getContent())
                .imageUrl(imageURl)
                .timestamp(new Date())
                .build();

        repository.save(savedMessage);
        return savedMessage;
    }

    public List<ChatMessage> getMessages(String chatRoomId) {

        return repository.findByChatRoomId(chatRoomId).orElse(null);
    }
}
