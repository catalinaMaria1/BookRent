package com.example.bookrent;

public class User {
    private int id;
    private String fullname;
    private String password;
    private String email;
    private String bio;

    public User(int id, String fullname, String password, String email, String bio) {
        this.id = id;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.bio = bio;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFullname() {
        return fullname;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }
}
