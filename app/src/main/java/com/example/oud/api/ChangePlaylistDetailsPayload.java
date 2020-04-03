package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class ChangePlaylistDetailsPayload {

    private String name;

    @SerializedName("public")
    private boolean _public;

    private boolean collaborative;

    private String description;



    /*"name": "string",
            "public": true,
            "collaborative": true,
            "description": "string",
            "image/png": "string"*/

}
