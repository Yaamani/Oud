package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class UserOrArtistPreview {
    @SerializedName("_id")
    String id ;
    @SerializedName("displayName")
    String name;
    String type;
    @SerializedName("image")
    String imageUrl;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
