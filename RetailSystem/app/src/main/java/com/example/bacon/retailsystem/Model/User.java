package com.example.bacon.retailsystem.Model;

public class User {

    String id, email, password, username, profileImage;

    public User() {
        id = "";
        email = "";
        password = "";
        username = "";
        profileImage = "";
    }

    public User(String id, String email, String password, String username, String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }
}
