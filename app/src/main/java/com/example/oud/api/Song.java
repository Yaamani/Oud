package com.example.oud.api;

public class Song {
    private String id;
    private String name;
    private String albumId;
    private String type;
    private String audioUrl;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getType() {
        return type;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public int getDuration() {
        return duration;
    }

    public int getViews() {
        return views;
    }

    public ArtistPreview[] getArtists() {
        return artists;
    }

    public Song(String id, String name, String albumId, String type, String audioUrl, int duration, int views, ArtistPreview[] artists) {
        this.id = id;
        this.name = name;
        this.albumId = albumId;
        this.type = type;
        this.audioUrl = audioUrl;
        this.duration = duration;
        this.views = views;
        this.artists = artists;
    }

    private int duration;
    private int views;
    private ArtistPreview[] artists;
}
