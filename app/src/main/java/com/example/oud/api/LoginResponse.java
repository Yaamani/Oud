package com.example.oud.api;

public class LoginResponse {
    private LoggedInUser user;

    public LoggedInUser getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public LoginResponse(LoggedInUser user, String token) {
        this.user = user;
        this.token = token;
    }

    private String token;
}
