package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class CurrentPlayback {

    @SerializedName("track")
    private Track mTrack;


    public Track getTrack() {
        return mTrack;
    }
}
