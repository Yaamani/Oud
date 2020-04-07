package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Album {

    private String _id;

    @SerializedName("album_type")
    private String albumType;

    private ArtistPreview[] artists;

    private String[] genres;

    private String image;

    private String name;

    @SerializedName("release_name")
    private Date releaseDate;

    private OudList<TrackPreview> tracks;

    private String type;

    public Album(String _id,
                 String albumType,
                 ArtistPreview[] artists,
                 String[] genres,
                 String image,
                 String name,
                 Date releaseDate,
                 OudList<TrackPreview> tracks,
                 String type) {
        this._id = _id;
        this.albumType = albumType;
        this.artists = artists;
        this.genres = genres;
        this.image = image;
        this.name = name;
        this.releaseDate = releaseDate;
        this.tracks = tracks;
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public String getAlbumType() {
        return albumType;
    }

    public ArtistPreview[] getArtists() {
        return artists;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public OudList<TrackPreview> getTracks() {
        return tracks;
    }

    public String getType() {
        return type;
    }
}
