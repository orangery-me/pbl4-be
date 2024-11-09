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
    private String photoURL;
    private String email;
    private Status status;

    @Override
    public String toString() {
        return "User [email=" + email + ", fullname=" + fullname + ", photoURL=" + photoURL + ", status=" + status
                + ", uid=" + uid + "]";
    }
}
