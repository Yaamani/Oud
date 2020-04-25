package com.example.oud.api;

public class ArtistPreview {
    private String _id;
    private String displayName;
    private String type;
    private String image;

    public ArtistPreview(String _id, String displayName, String type, String image) {
        this._id = _id;
        this.displayName = displayName;
        this.type = type;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }
}
