package com.example.oud.api;

public class Artist {
    private String _id;
    private int followersCount;
    private String[] genres;
    private String[] images;
    private String name;
    private String bio;
    private Track[] popularSongs;
    private String type;

    public String get_id() {
        return _id;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String[] getGenres() {
        return genres;
    }

    public String[] getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public Track[] getPopularSongs() {
        return popularSongs;
    }

    public String getType() {
        return type;
    }

    public Artist(String _id,
                  int followersCount,
                  String[] genres,
                  String[] images,
                  String name,
                  String bio,
                  Track[] popularSongs,
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
