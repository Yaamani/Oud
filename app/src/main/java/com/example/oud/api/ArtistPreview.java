package com.example.oud.api;

public class ArtistPreview {
    private String _id;
    private String name;
    private String type;
    private String image;

    public ArtistPreview(String _id, String name, String type, String image) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }
}
