package com.wsserver.pbl4.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wsserver.pbl4.models.Status;
import com.wsserver.pbl4.models.User;
import com.wsserver.pbl4.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository repository;

    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        try {
            repository.save(user);
            System.out.println("User saved! ");
        } catch (Exception e) {
            System.out.println("Error saving user: " + e);
        }
    }

    public void disconnectUser(User user) {
        var storedUser = repository.findById(user.getUid()).orElse(null);
        if (storedUser == null) {
            return;
        }
        storedUser.setStatus(Status.OFFLINE);
        repository.save(storedUser);
    }

    public List<User> findConnectedUsers() {
        return repository.findByStatus(Status.ONLINE);
    }
}
