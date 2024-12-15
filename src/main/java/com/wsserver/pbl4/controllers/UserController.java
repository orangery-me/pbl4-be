package com.wsserver.pbl4.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wsserver.pbl4.models.User;
import com.wsserver.pbl4.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
public class UserController {
    @Autowired
    private final UserService userService;

    // /app/addUser
    @MessageMapping("/addUser") // client will send message to this endpoint
    @SendTo("/user/topic") // return value will be sent to this topic
    public User addUser(@Payload User user) {
        System.out.println("Saving user nek: " + user.toString());
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/disconnectUser") // client will send message to this endpoint
    @SendTo("/user/topic") // return value will be sent to this topic
    public User disconnectUser(@Payload User user) {
        userService.disconnectUser(user);
        return user;
    }

    @GetMapping("/connectedUsers")
    public ResponseEntity<List<User>> getConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/findByFullname")
    public ResponseEntity<List<User>> getMethodName(@RequestParam("name") String name) {
        return ResponseEntity.ok(userService.findByFullname(name));
    }
    @GetMapping("/findByEmail")
    public ResponseEntity<List<User>> getUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
    @GetMapping("/findById")
    public ResponseEntity<User> getUserById(@RequestParam("Id") String Id) {
        return ResponseEntity.ok(userService.findById(Id));
    }
    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
    
}
