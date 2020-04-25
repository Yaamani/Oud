package com.example.oud.api;

public class LoginUserInfo {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginUserInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

