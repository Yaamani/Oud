package com.example.oud.api;

import java.util.ArrayList;

public class Artist {
    private String _id;
    private int followersCount;
    private ArrayList<Genre> genres;
    private ArrayList<String> images;
    private String displayName;
    private String bio;
    private ArrayList<Track> popularSongs;
    private String type;

    public String get_id() {
        return _id;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public ArrayList<Track> getPopularSongs() {
        return popularSongs;
    }

    public String getType() {
        return type;
    }

    public Artist(String _id,
                  int followersCount,
                  ArrayList<Genre> genres,
                  ArrayList<String> images,
                  String displayName,
                  String bio,
                  ArrayList<Track> popularSongs,
                  String type) {
        this._id = _id;
        this.followersCount = followersCount;
        this.genres = genres;
        this.images = images;
        this.displayName = displayName;
        this.bio = bio;
        this.popularSongs = popularSongs;
        this.type = type;
    }
}
