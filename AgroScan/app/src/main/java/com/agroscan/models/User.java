package com.agroscan.models;

public class User {
    public String username;
    public String fullName;
    public String email;
    public boolean isAdmin;

    public User(String username, String fullName, String email, boolean isAdmin) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.isAdmin = isAdmin;
    }
}
