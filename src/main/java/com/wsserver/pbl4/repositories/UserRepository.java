package com.wsserver.pbl4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wsserver.pbl4.models.Status;
import com.wsserver.pbl4.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findByStatus(Status status);

    public List<User> findByFullname(String fullname);

    public List<User> findByEmail(String email);

    public Optional<User> findById(String id);
}
