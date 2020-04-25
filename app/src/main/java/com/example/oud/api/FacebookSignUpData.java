package com.example.oud.api;

public class FacebookSignUpData {


    String facebook_id;
    String email;
    String gender;
    String displayName;
    String[] images;
    String birthDate;

    public FacebookSignUpData(String facebook_id, String email, String gender, String displayName, String[] images, String birthDate) {
        this.facebook_id = facebook_id;
        this.email = email;
        this.gender = gender;
        this.displayName = displayName;
        this.images = images;
        this.birthDate = birthDate;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getImages() {
        return images;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
