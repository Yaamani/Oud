package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class LoggedInUser {

    private String _id;
    private String username;
    private String birthDate;
    private String gender;
    private String email;
    private String displayName;
    private String role;
    private String country;
    private String[] images;
    private String facebook_id;
    private String google_id;
    private int credit;
    private int followersCount;
    private String plan;
    private boolean verified;
    private String type;

    @SerializedName("com.example.oud.api.Artist")
    private Artist artist;

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public String getCountry() {
        return country;
    }

    public String[] getImages() {
        return images;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public int getCredit() {
        return credit;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getPlan() {
        return plan;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getType() {
        return type;
    }

    public Artist getArtist() {
        return artist;
    }

    public LoggedInUser(String _id, String username, String birthDate, String gender, String email, String displayName, String role, String country, String[] images, String facebook_id, String google_id, int credit, int followersCount, String plan, boolean verified, String type, Artist artist) {
        this._id = _id;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.country = country;
        this.images = images;
        this.facebook_id = facebook_id;
        this.google_id = google_id;
        this.credit = credit;
        this.followersCount = followersCount;
        this.plan = plan;
        this.verified = verified;
        this.type = type;
        this.artist = artist;
    }
}
