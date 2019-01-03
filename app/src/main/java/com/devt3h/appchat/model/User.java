package com.devt3h.appchat.model;

public class User {
<<<<<<< HEAD
    private String name;
    private String email;
    private String avatarURL;
    private String id;

    public User(String name, String email, String avatarURL, String id) {
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.id = id;
=======
    private String id;
    private String name;
    private String avatarURL;

    public User(String id, String name, String avatarURL) {
        this.id = id;
        this.name = name;
        this.avatarURL = avatarURL;
>>>>>>> 360d8d1a07eddebc4e45cd98871af9f90f8a0f1b
    }

    public User() {
    }

<<<<<<< HEAD
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

=======
>>>>>>> 360d8d1a07eddebc4e45cd98871af9f90f8a0f1b
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

<<<<<<< HEAD
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
=======
>>>>>>> 360d8d1a07eddebc4e45cd98871af9f90f8a0f1b

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
<<<<<<< HEAD
=======

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
>>>>>>> 360d8d1a07eddebc4e45cd98871af9f90f8a0f1b
}
