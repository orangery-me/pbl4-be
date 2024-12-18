package com.wsserver.pbl4.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wsserver.pbl4.DTOs.PrivateChatMessageRequest;
import com.wsserver.pbl4.models.PrivateChatMessage;
import com.wsserver.pbl4.models.User;
import com.wsserver.pbl4.repositories.PrivateChatMessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrivateChatMessageService {
    private PrivateChatMessageRepository repository;
    private PrivateChatRoomService roomService;
    private UserService userService;
    private CloudinaryService cloudinaryService;

   
    public PrivateChatMessage saveMessage(PrivateChatMessageRequest message) {
        User sender = null;
        User receiver = null;
        try {
            sender = userService.findById(message.getSenderId());
            receiver = userService.findById(message.getReceiverId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String chatRoomId = roomService.getPrivateChatRoomId(message.getSenderId(), message.getReceiverId())
                .orElseGet(() -> {
                    return roomService.createPrivateChatRoom(message.getSenderId(), message.getReceiverId());
                });

        String imageURl = "";
        if (message.getFile() != null && !message.getFile().isEmpty())
            imageURl = cloudinaryService.upload(message.getFile());

        PrivateChatMessage savedMessage = PrivateChatMessage.builder()
                .chatRoomId(chatRoomId)
                .sender(sender)
                .receiver(receiver)
                .content(message.getContent())
                .imageUrl(imageURl)
                .timestamp(new Date())
                .build();

        repository.save(savedMessage);
        return savedMessage;
    }

    public List<PrivateChatMessage> getMessages(String chatRoomId) {

        return repository.findByChatRoomId(chatRoomId).orElse(null);
    }

}
