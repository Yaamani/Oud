package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LikedTrack {

    @SerializedName("added_at")
    private Date addedAt;

    private Track track;

    public LikedTrack(Date addedAt, Track track) {
        this.addedAt = addedAt;
        this.track = track;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
