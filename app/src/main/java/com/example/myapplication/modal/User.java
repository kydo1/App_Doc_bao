package com.example.myapplication.modal;

public class User {
    private String _id;
    private String email;
    private String password;
    private  String fullName;
    private String avatar;
    private Boolean isAdmin;

    public User() {
    }

    public User(String _id, String email, String password, String fullName, String avatar, Boolean isAdmin) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.isAdmin = isAdmin;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
