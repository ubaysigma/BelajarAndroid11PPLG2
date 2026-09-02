package com.example.belajarandroid11pplg2;

public class UserModel {

    int id;
    String name;
    String username;
    String email;

    public UserModel(
            int id,
            String name,
            String username,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}