package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AlbumPreview {

    @SerializedName("_id")
    private String id;

    private String albumType;

    private String albumGroup;

    private ArrayList<ArtistPreview> artists;

    private String image;

    private String name;

    private String type;

    private boolean released;

    public AlbumPreview(String id, String albumType, String albumGroup, ArrayList<ArtistPreview> artists, String image, String name, String type,boolean released) {
        this.id = id;
        this.albumType = albumType;
        this.albumGroup = albumGroup;
        this.artists = artists;
        this.image = image;
        this.name = name;
        this.type = type;
        this.released = released;
    }

    public boolean isReleased() {
        return released;
    }

    public String getId() {
        return id;
    }

    public String getAlbumType() {
        return albumType;
    }

    public String getAlbumGroup() {
        return albumGroup;
    }

    public ArrayList<ArtistPreview> getArtists() {
        return artists;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
