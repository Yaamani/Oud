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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPublicPlaylist(boolean publicPlaylist) {
        this.publicPlaylist = publicPlaylist;
    }

    public void setType(String type) {
        this.type = type;
    }
}
