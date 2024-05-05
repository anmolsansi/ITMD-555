package com.example.moviereview.model;

public class User {
    public String name;
    public String mobile;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String mobile, String email) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }
}

