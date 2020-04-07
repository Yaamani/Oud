package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class ProfilePreview {
    @SerializedName("_id")
    private String id;

    private String displayName;

    private String[] images;

    private String type;

    private boolean verified;

    private int followersCount;

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getImages() {
        return images;
    }

    public String getType() {
        return type;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getFollowersCount() {
        return followersCount;
    }
}
