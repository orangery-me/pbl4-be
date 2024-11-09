package com.wsserver.pbl4.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    @Id
    private String id;
    private String roomName;
    private String avatar;
    private String roomOwnerId;
    private List<String> membersId;
}
