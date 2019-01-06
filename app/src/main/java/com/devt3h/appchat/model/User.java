package com.devt3h.appchat.model;

public class User {
    private String name;
    private String email;
    private String avatarURL;
    private String id;

    public User(String name, String email, String avatarURL, String id) {
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.id = id;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}