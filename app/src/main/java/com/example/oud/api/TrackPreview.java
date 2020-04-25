package com.example.oud.api;

public class TrackPreview {
    private String _id;
    private String name;
    private String type;
    private int duration;
    private int views;
    private ArtistPreview[] artists;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
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

    public TrackPreview(String _id,
                        String name,
                        String type,
                        int duration,
                        int views,
                        ArtistPreview[] artists) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.views = views;
        this.artists = artists;
    }


}
