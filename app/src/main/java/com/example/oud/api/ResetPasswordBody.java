package com.example.oud.api;

public class ResetPasswordBody {
    private String password;
    private String passwordConfirm;

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public ResetPasswordBody(String password, String passwordConfirm) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}
