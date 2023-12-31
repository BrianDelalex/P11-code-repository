package com.medhead.POC.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {
    @Id
    public String id;
    public String username;
    public String password;

    User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}