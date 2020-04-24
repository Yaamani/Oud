package com.example.oud.api;

public class Track {
    private String _id;
    private String name;
    private String albumId;
    private AlbumPreview album;
    private String type;
    private String audioUrl;
    private int duration;
    private int views;
    private ArtistPreview[] artists;


    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAlbumId() {
        return albumId;
    }

    public AlbumPreview getAlbum() {
        return album;
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

    public Track(String _id,
                 String name,
                 String albumId,
                 AlbumPreview album,
                 String type,
                 String audioUrl,
                 int duration,
                 int views,
                 ArtistPreview[] artists) {
        this._id = _id;
        this.name = name;
        this.albumId = albumId;
        this.album = album;
        this.type = type;
        this.audioUrl = audioUrl;
        this.duration = duration;
        this.views = views;
        this.artists = artists;
    }


}
