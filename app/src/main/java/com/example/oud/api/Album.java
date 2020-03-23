package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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



    private String type;
}
