package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SavedAlbum {

    @SerializedName("added_at")
    private Date addedAt;

    private Album album;

    public SavedAlbum(Date addedAt, Album album) {
        this.addedAt = addedAt;
        this.album = album;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
