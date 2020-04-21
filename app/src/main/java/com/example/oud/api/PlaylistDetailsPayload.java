package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class PlaylistDetailsPayload {

    private String name;

    @SerializedName("public")
    private Boolean _public;

    private Boolean collaborative;

    private String description;

    private MultipartBody.Part image;

    public PlaylistDetailsPayload(String name,
                                  Boolean _public,
                                  Boolean collaborative,
                                  String description,
                                  MultipartBody.Part image) {
        this.name = name;
        this._public = _public;
        this.collaborative = collaborative;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean is_public() {
        return _public;
    }

    public void set_public(Boolean _public) {
        this._public = _public;
    }

    public Boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }
}
