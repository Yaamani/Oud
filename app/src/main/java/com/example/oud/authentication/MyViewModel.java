package com.example.oud.authentication;

import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private boolean signupWithFacebook= false;
    private boolean signupWithGoogle = false;
    private String email;
    private String gender;
    private String displayName;
    private String birthDate;

    public boolean isSignupWithGoogle() {
        return signupWithGoogle;
    }

    public void setSignupWithGoogle(boolean signupWithGoogle) {
        this.signupWithGoogle = signupWithGoogle;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isSignupWithFacebook() {
        return signupWithFacebook;
    }

    public void setSignupWithFacebook(boolean signupWithFacebook) {
        this.signupWithFacebook = signupWithFacebook;
    }

    // TODO: Implement the ViewModel


}
