package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Playlist {

    @SerializedName("_id")
    private String id;

    private String name;

    private String owner;

    private boolean collaborative;

    private String description;

    private int followersCount;

    private ArrayList<Track> tracks;

    private String image;

    @SerializedName("public")
    private boolean publicPlaylist;

    private String type;

    public Playlist(String id,
                    String name,
                    String owner,
                    boolean collaborative,
                    String description,
                    int followersCount,
                    ArrayList<Track> tracks,
                    String image,
                    boolean publicPlaylist, String type) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.collaborative = collaborative;
        this.description = description;
        this.followersCount = followersCount;
        this.tracks = tracks;
        this.image = image;
        this.publicPlaylist = publicPlaylist;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public String getImage() {
        return image;
    }

    public boolean isPublicPlaylist() {
        return publicPlaylist;
    }

    public String getType() {
        return type;
    }
}
