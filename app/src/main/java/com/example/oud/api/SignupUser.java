package com.example.oud.api;

import java.util.Date;

public class SignupUser {
    private String username ;
    private String displayName;
    private String birthDate;
    private String email;
    private String password;
    private String passwordConfirm;
    private String role;
    private String country;
    private String gender;

    public SignupUser(String username, String displayName, String birthDate, String email, String password, String passwordConfirm, String role, String country, String gender) {
        this.username = username;
        this.displayName = displayName;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.role = role;
        this.country = country;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getRole() {
        return role;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }
}

