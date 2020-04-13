package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserAreTracksLiked {

    @SerializedName("IsFound")
    ArrayList<Boolean> isFound;

    public UserAreTracksLiked(ArrayList<Boolean> isFound) {
        this.isFound = isFound;
    }

    public ArrayList<Boolean> getIsFound() {
        return isFound;
    }
}
