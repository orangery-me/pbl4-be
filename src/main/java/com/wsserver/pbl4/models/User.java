package com.wsserver.pbl4.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String uid;
    private String fullname;
    private Status status;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", fullname='" + fullname + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
