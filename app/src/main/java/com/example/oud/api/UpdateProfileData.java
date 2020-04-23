package com.example.oud.api;

public class UpdateProfileData {
    String email;
    String passwordConfirm;
    String gender;
    String dateOfBirth;
    String country;
    String displayName;

    public UpdateProfileData(String email, String passwordConfirm, String gender, String dateOfBirth, String country, String displayName) {
        this.email = email;
        this.passwordConfirm = passwordConfirm;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.displayName = displayName;
    }
}
