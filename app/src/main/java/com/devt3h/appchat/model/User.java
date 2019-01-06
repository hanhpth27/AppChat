package com.devt3h.appchat.model;

public class User {
    private String full_name;
    private String email;
    private String avatarURL;
    private String id;
    private String birthday;


    public User() {
    }

    public User(String full_name, String email, String avatarURL, String id, String birthday) {
        this.full_name = full_name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.id = id;
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
