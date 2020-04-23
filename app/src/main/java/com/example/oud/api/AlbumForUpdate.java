package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class AlbumForUpdate {
    @SerializedName("_id")
    private String albumId;

    @SerializedName("album_type")
    private String albumType;

    private ArrayList<String> artists;

    private ArrayList<String> genres;


    private String name;

    @SerializedName("release_date")
    private String releaseDate;

    //todo change name when backend adds the variable to the api





    public AlbumForUpdate(String albumType, ArrayList<String> artists, ArrayList<String> genres, String name, String releaseDate,String albumId) {
        this.albumType = albumType;
        this.artists = artists;
        this.genres = genres;
        this.name = name;
        this.releaseDate = releaseDate;
        this.albumId = albumId;
    }



    public String getAlbumType() {
        return albumType;
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }


    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }




}
