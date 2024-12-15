package com.wsserver.pbl4.models;

import org.springframework.web.socket.WebSocketSession;

public class UserSession {
    private String userId;
    private WebSocketSession session;

    public UserSession(String userId, WebSocketSession session) {
        this.userId = userId;
        this.session = session;
    }

    public String getUserId() {
        return userId;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
