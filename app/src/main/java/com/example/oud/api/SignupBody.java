package com.example.oud.api;

public class SignupBody {
    Device device;
    SignupUser user;

    public SignupBody(Device device, SignupUser user) {
        this.device = device;
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public SignupUser getUser() {
        return user;
    }
}
