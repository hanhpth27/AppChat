package com.devt3h.appchat.model;

public class User {
    private String id;
    private String name;
    private String avatarURL;

    public User(String id, String name, String avatarURL) {
        this.id = id;
        this.name = name;
        this.avatarURL = avatarURL;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
