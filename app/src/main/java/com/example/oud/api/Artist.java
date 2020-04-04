package com.example.oud.api;

import java.util.ArrayList;

public class Artist {
    private String _id;
    private int followersCount;
    private ArrayList<String> genres;
    private ArrayList<String> images;
    private String name;
    private String bio;
    private ArrayList<Track> popularSongs;
    private String type;

    public String get_id() {
        return _id;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getName() {
        return name;
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
                  ArrayList<String> genres,
                  ArrayList<String> images,
                  String name,
                  String bio,
                  ArrayList<Track> popularSongs,
                  String type) {
        this._id = _id;
        this.followersCount = followersCount;
        this.genres = genres;
        this.images = images;
        this.name = name;
        this.bio = bio;
        this.popularSongs = popularSongs;
        this.type = type;
    }
}
