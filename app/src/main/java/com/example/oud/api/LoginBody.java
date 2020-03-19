package com.example.oud.api;

public class LoginBody {
    Device device;
    LoginUserInfo user;

    public LoginBody(Device device, LoginUserInfo user) {
        this.device = device;
        this.user = user;
    }
}
